package com.popcorn.utils;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.compiler.parser.PopcornParser;
import com.popcorn.node.ExpressionNode;
import com.popcorn.utils.diagnostics.Diagnostic;

import java.util.List;

public class SyntaxTree {

    private List<Diagnostic> diagnostics;
    private ExpressionNode root;
    private Token endOfFileToken;

    public SyntaxTree(List<Diagnostic> diagnostics, ExpressionNode root, Token endOfFileToken) {
        this.diagnostics = diagnostics;
        this.root = root;
        this.endOfFileToken = endOfFileToken;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public ExpressionNode getRoot() {
        return root;
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
