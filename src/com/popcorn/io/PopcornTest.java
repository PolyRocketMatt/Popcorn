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

        compileTest(testAssignment());
        compileTest(testPrint());

        delta = System.currentTimeMillis() - delta;

        System.out.println(MessageFormat.format("Test Compilation in [{0}ms]", delta));
    }

    private void compileTest(String source) throws PopcornException {
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
