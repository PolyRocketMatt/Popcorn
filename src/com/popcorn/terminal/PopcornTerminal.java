package com.popcorn.terminal;

import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.parser.PopcornParser;
import com.popcorn.utils.Diagnostics;
import com.popcorn.utils.Filters;
import com.popcorn.utils.utilities.PrintUtils;

import java.util.LinkedHashSet;
import java.util.Scanner;

public class PopcornTerminal {

    public PopcornTerminal() {
        Diagnostics diagnostics = new Diagnostics();

        if (!diagnostics.getDiagnostics().isEmpty()) {
            for (String error : diagnostics.getDiagnostics()) {
                System.out.println(error);
            }
        } else {
            System.out.println("Popcorn Compiler v0.0.1");
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String instruction = scanner.nextLine();

            boolean isPrint = false;

            while (!instruction.equals("exit")) {
                if (instruction.equals("tree")) {
                    isPrint = !isPrint;
                } else {
                    // TODO: 02/02/2020 Implement compiler
                    Tokenizer tokenizer = Tokenizer.getTokenizer(diagnostics, instruction);

                    tokenizer.tokenize();
                    diagnostics.getDiagnostics().addAll(tokenizer.getDiagnostics().getDiagnostics());
                    LinkedHashSet<String> filtered = Filters.filterDuplicates(diagnostics.getDiagnostics());

                    if (!filtered.isEmpty()) {
                        for (String err : filtered) {
                            System.out.println(err);
                        }
                    } else {
                        PopcornParser parser = new PopcornParser(diagnostics, tokenizer.getStream());
                        Node root = parser.parse();

                        if (root != null) {
                            if (isPrint)
                                PrintUtils.prettyPrint(root, "", true);
                        }

                        if (!parser.getDiagnostics().getDiagnostics().isEmpty()) {
                            for (String err : parser.getDiagnostics().getDiagnostics()) {
                                System.out.println(err);
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
