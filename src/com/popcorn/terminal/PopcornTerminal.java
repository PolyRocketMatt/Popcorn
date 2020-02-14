package com.popcorn.terminal;

import com.popcorn.compiler.Compilation;
import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.utils.Filters;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.Diagnostic;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.PrintUtils;
import com.popcorn.utils.values.LiteralValue;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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
            System.out.println("Popcorn Compiler v0.0.1");
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String instruction = scanner.nextLine();

            Compilation compilation = null;

            boolean isPrint = false;
            boolean isTable = false;
            boolean isFile = false;

            while (!instruction.equals("exit")) {
                if (isFile) {
                    try {
                        File file = new File(instruction);
                        Scanner fileScanner = new Scanner(file);
                        StringBuilder source = new StringBuilder("");

                        while (fileScanner.hasNextLine()) {
                            source.append(fileScanner.nextLine());
                        }
                    } catch (Exception ex) { }
                }

                boolean isFinished = false;

                if (instruction.equals("tree")) {
                    isPrint = !isPrint;
                    isFinished = true;
                    System.out.println("Printing Tree: " + isPrint);
                } else if (instruction.equals("table")) {
                    isTable = !isTable;
                    isFinished = true;
                    System.out.println("Printing Tables: " + isTable);
                } else if (instruction.startsWith("file")) {
                    isFile = !isFile;
                    instruction = "";
                    System.out.println("Expecting File: " + isFile);
                }

                if (!isFinished) {
                    // TODO: 02/02/2020 Implement compiler
                    Tokenizer tokenizer = Tokenizer.getTokenizer(instruction);

                    tokenizer.tokenize();
                    diagnostics.getDiagnostics().addAll(tokenizer.getDiagnostics().getDiagnostics());

                    if (false) {
                        System.out.println(tokenizer.getStream().toString());
                    } else {
                        ArrayList<String> messages = new ArrayList<>();
                        diagnostics.getDiagnostics().forEach(diagnostic -> messages.add(diagnostic.getMessage()));
                        LinkedHashSet<String> filtered = Filters.filterDuplicates(messages);

                        if (!filtered.isEmpty()) {
                            for (String err : filtered) {
                                System.out.println(err);
                            }
                        } else {
                            try {
                                if (compilation == null)
                                    compilation = new Compilation(Objects.requireNonNull(SyntaxTree.parse(instruction)));
                                else
                                    compilation.setTree(SyntaxTree.parse(instruction));

                                compilation.exec();

                                for (LiteralValue value : compilation.getValues()) {
                                    if (value.getValue() == null)
                                        System.out.println("null");
                                    else
                                        System.out.println(value.getValue().toString());
                                }

                                // TODO: 10/02/2020 Implement proper error reporting!
                                if (!compilation.getDiagnostics().isEmpty()) {
                                    for (Diagnostic diagnostic : compilation.getDiagnostics()) {
                                        System.out.println(diagnostic.getMessage());
                                    }

                                    //Clearing current diagnostics
                                    compilation.getDiagnostics().clear();
                                } else {
                                    if (isPrint)
                                        PrintUtils.prettyPrint(compilation.getTree().getParentNode(), "", true);
                                    if (isTable)
                                        PrintUtils.prettyPrint(compilation.getInterpreter().getVariables());
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                diagnostics.getDiagnostics().clear();

                System.out.print("> ");
                instruction = scanner.nextLine();
            }
        }
    }

}
