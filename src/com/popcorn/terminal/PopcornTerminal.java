package com.popcorn.terminal;

import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.utils.Diagnostics;
import com.popcorn.utils.Filters;

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

            while (!instruction.equals("exit")) {
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
                    System.out.println(tokenizer.getStream().toString());
                }

                System.out.print("> ");
                instruction = scanner.nextLine();
            }
        }
    }

}
