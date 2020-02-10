package com.popcorn.terminal;

import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.compiler.parser.PopcornParser;
import com.popcorn.node.Node;
import com.popcorn.utils.Filters;
import com.popcorn.utils.diagnostics.Diagnostic;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.PrintUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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

            boolean isPrint = false;

            while (!instruction.equals("exit")) {
                if (instruction.equals("tree")) {
                    isPrint = !isPrint;
                } else {
                    // TODO: 02/02/2020 Implement compiler
                    Tokenizer tokenizer = Tokenizer.getTokenizer(instruction);

                    tokenizer.tokenize();
                    diagnostics.getDiagnostics().addAll(tokenizer.getDiagnostics().getDiagnostics());

                    ArrayList<String> messages = new ArrayList<>();
                    diagnostics.getDiagnostics().forEach(diagnostic -> messages.add(diagnostic.getMessage()));
                    LinkedHashSet<String> filtered = Filters.filterDuplicates(messages);

                    if (!filtered.isEmpty()) {
                        for (String err : filtered) {
                            System.out.println(err);
                        }
                    } else {
                        try {
                            PopcornParser parser = new PopcornParser(diagnostics, tokenizer.getStream());
                            Node root = parser.parse();

                            if (root != null) {
                                if (isPrint)
                                    PrintUtils.prettyPrint(root, "", true);
                            }

                            if (!parser.getDiagnostics().getDiagnostics().isEmpty()) {
                                for (Diagnostic error : parser.getDiagnostics().getDiagnostics()) {
                                    System.out.println(error.getMessage());
                                }
                            }
                        } catch (Exception ex) {}
                    }
                }

                diagnostics.getDiagnostics().clear();

                System.out.print("> ");
                instruction = scanner.nextLine();
            }
        }
    }

}
