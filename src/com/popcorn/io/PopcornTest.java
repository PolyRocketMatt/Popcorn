package com.popcorn.io;

import com.popcorn.compiler.Compilation;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.Diagnostic;
import com.popcorn.utils.utilities.PrintUtils;

import java.text.MessageFormat;
import java.util.Objects;

public class PopcornTest {

    /**
     * This class tests the full pipeline of the Popcorn Compiler
     */
    public PopcornTest() throws PopcornException {
        long delta = System.currentTimeMillis();

        compileTest(testAssignment(), 1);
        compileTest(testAssignment_2(), 2);
        compileTest(testComment(), 3);
        compileTest(testIf(), 4);
        compileTest(testPrint(), 5);

        delta = System.currentTimeMillis() - delta;

        System.out.println(MessageFormat.format("Test Compilation in [{0}ms]", delta));
    }

    private void compileTest(String source, int testNumber) throws PopcornException {
        long deltaParseTime = System.currentTimeMillis();
        SyntaxTree tree = Objects.requireNonNull(SyntaxTree.parse(source));

        deltaParseTime = System.currentTimeMillis() - deltaParseTime;

        Compilation compilation = new Compilation(tree, deltaParseTime);
        compilation.exec();

        // TODO: 10/02/2020 Implement proper error reporting!
        if (compilation.getDiagnostics().isEmpty() && compilation.wasSuccessful()) {
            PrintUtils.prettyPrint(compilation.getTree().getParentNode(), "", true);
            PrintUtils.prettyPrint(compilation.getInterpreter().getVariables());
            System.out.println(MessageFormat.format("Compilation in [{0}ms]", compilation.getCompilationTime()));
        } else {
            for (Diagnostic diagnostic : compilation.getDiagnostics())
                System.out.println(diagnostic.getMessage());
            compilation.getDiagnostics().clear();

            System.out.println("Test Failed");
        }

        System.out.println("\n\n");
    }

    private String testAssignment() {
        return "class testAssignment() {\n" +
                "   int a = 5\n" +
                "   int b = 6\n" +
                "   print(a)\n" +
                "   print(b)\n" +
                "   print(a + b)\n" +
                "   print(a - b)\n" +
                "   print(a * b)\n" +
                "   print(a / b)\n" +
                "   print(a % b)\n" +
                "}";
    }

    private String testAssignment_2() {
        return "class AssignmentTest() {\n" +
                "\n" +
                "    int a = 5\n" +
                "    int b = 6\n" +
                "    int a = 10\n" +
                "    float b = 3.3\n" +
                "    float d = 3.3\n" +
                "    int e = 2 + 7\n" +
                "    float f = 2.2 + 9\n" +
                "    string hello = \"Hello, World!\"\n" +
                "    bool val = ((3 + 7) == 9)\n" +
                "\n" +
                "    print(a)\n" +
                "    print(b)\n" +
                "    print(d)\n" +
                "    print(e)\n" +
                "    print(f)\n" +
                "    print(hello)\n" +
                "    print(val)\n" +
                "\n" +
                "}";
    }

    private String testComment() {
        return "class testCommentStatement() {\n" +
                "\n" +
                "    //This line should not be seen as actual code\n" +
                "    //This is just a regular comment\n" +
                "    //Comments can be declared using a double slash\n" +
                "\n" +
                "}";
    }

    private String testIf() {
        return "class testIfStatements() {\n" +
                "\n" +
                "    //This should print \"true\"\n" +
                "    if ((true || false) && ((2 == 1 + 2 - 1) || false)) {\n" +
                "        print(true)\n" +
                "    } else {\n" +
                "        print(false)\n" +
                "    }\n" +
                "\n" +
                "    //This should print false\n" +
                "    if (((false && true) && true) && (2 - 2 == 0)) {\n" +
                "        print(true)\n" +
                "    } else {\n" +
                "        print(false)\n" +
                "    }\n" +
                "\n" +
                "}";
    }

    private String testPrint() {
        return "class testPrint() {\n" +
                "   int a = 5\n" +
                "   float b = 6.6\n" +
                "   string c = \"Hello, World!\"\n" +
                "   bool d = false\n" +
                "   print(a)\n" +
                "   print(b)\n" +
                "   print(c)\n" +
                "   print(d)\n" +
                "   print(a + b)\n" +
                "   print(b + \" Huh?\")\n" +
                "   print(c + \"Huh?\")\n" +
                "   print(false == d)\n" +
                "   print(\"Boo!\")\n" +
                "}";
    }

}
