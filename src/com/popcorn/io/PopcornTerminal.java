package com.popcorn.io;

import com.popcorn.compiler.Compilation;
import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.Diagnostic;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.PrintUtils;

import java.util.Objects;
import java.util.Scanner;

public class PopcornTerminal {

    public PopcornTerminal() {
        DiagnosticsBag diagnostics = new DiagnosticsBag();

        if (!diagnostics.getDiagnostics().isEmpty()) {
            for (Diagnostic error : diagnostics.getDiagnostics()) {
                System.out.println(error.getMessage());
            }
        } else {
            Compilation compilation = null;
            System.out.println("Popcorn Compiler v0.0.1");
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String instruction = scanner.nextLine();

            boolean isPrint = false;
            boolean isTable = false;
            while (!instruction.equals("exit")) {
                if (instruction.equals("tree")) {
                    isPrint = !isPrint;
                    System.out.println("Printing Tree: " + isPrint);
                } else if (instruction.equals("table")) {
                    isTable = !isTable;
                    System.out.println("Printing Tables: " + isTable);
                } else if (instruction.equals("test")) {
                    try {
                        new PopcornTest();
                    } catch (PopcornException ex) {
                        System.out.println("Test failed!");
                    }
                } else {
                    try {
                        if (compilation == null)
                            compilation = new Compilation(Objects.requireNonNull(SyntaxTree.parse(instruction)));
                        else
                            compilation.setTree(SyntaxTree.parse(instruction));
                        compilation.exec();

                        // TODO: 10/02/2020 Implement proper error reporting!
                        if (compilation.getDiagnostics().isEmpty()) {
                            if (isPrint)
                                PrintUtils.prettyPrint(compilation.getTree().getParentNode(), "", true);
                            if (isTable)
                                PrintUtils.prettyPrint(compilation.getInterpreter().getVariables());
                        } else {
                            for (Diagnostic diagnostic : compilation.getDiagnostics())
                                System.out.println(diagnostic.getMessage());
                            compilation.getDiagnostics().clear();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                diagnostics.getDiagnostics().clear();

                System.out.print("> ");
                instruction = scanner.nextLine();
            }
        }
    }

}
