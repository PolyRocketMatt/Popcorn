package com.popcorn.io;

import com.popcorn.compiler.Compilation;
import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.Diagnostic;
import com.popcorn.utils.utilities.PrintUtils;

import java.util.Objects;

public class PopcornTest {

    /**
     * This class tests the full pipeline of the Popcorn Compiler
     */
    public PopcornTest() throws PopcornException {
        String source = "class test() {" + "\n" +
                "   int a = 5" + "\n" +
                "   int b = 5" + "\n" +
                "   if (a == b) {" + "\n" +
                "       print(0)" + "\n" +
                "   } else {" + "\n" +
                "       print(1)" + "\n" +
                "   }" + "\n" +
                "}";
        Compilation compilation = new Compilation(Objects.requireNonNull(SyntaxTree.parse(source)));
        compilation.exec();

        // TODO: 10/02/2020 Implement proper error reporting!
        if (compilation.getDiagnostics().isEmpty()) {
            PrintUtils.prettyPrint(compilation.getTree().getParentNode(), "", true);
            PrintUtils.prettyPrint(compilation.getInterpreter().getVariables());
        } else {
            for (Diagnostic diagnostic : compilation.getDiagnostics())
                System.out.println(diagnostic.getMessage());
            compilation.getDiagnostics().clear();
        }
    }

}
