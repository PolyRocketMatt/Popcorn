package com.popcorn.utils;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.compiler.node.ParentNode;
import com.popcorn.compiler.parser.PopcornParser;
import com.popcorn.utils.diagnostics.Diagnostic;

import java.util.List;

public class SyntaxTree {

    private List<Diagnostic> diagnostics;
    private ParentNode parentNode;
    private Token endOfFileToken;

    public SyntaxTree(List<Diagnostic> diagnostics, ParentNode root, Token endOfFileToken) {
        this.diagnostics = diagnostics;
        this.parentNode = root;
        this.endOfFileToken = endOfFileToken;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public ParentNode getParentNode() {
        return parentNode;
    }

    public Token getEndOfFileToken() {
        return endOfFileToken;
    }

    public static SyntaxTree parse(String source) {
        try {
            Tokenizer tokenizer = new Tokenizer();

            tokenizer.setSource(source);
            tokenizer.tokenize();

            PopcornParser parser = new PopcornParser(tokenizer.getDiagnostics(), tokenizer.getStream());

            // TODO: 10/02/2020 Do error reporting here! 
            
            return parser.parse();
        } catch (Exception ex) {
            System.out.println("Internal error");
            return null;
        }
    }
}
