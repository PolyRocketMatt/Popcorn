package com.popcorn.utils;

import com.popcorn.compiler.lexical.Tokenizer;
import com.popcorn.compiler.node.ParentNode;
import com.popcorn.compiler.parser.PopcornParser;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.diagnostics.Diagnostic;

import java.util.List;

public class SyntaxTree {

    private List<Diagnostic> diagnostics;
    private ParentNode parentNode;

    public SyntaxTree(List<Diagnostic> diagnostics, ParentNode root) {
        this.diagnostics = diagnostics;
        this.parentNode = root;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public ParentNode getParentNode() {
        return parentNode;
    }

    public static SyntaxTree parse(String source) throws PopcornException {
        try {
            Tokenizer tokenizer = new Tokenizer();

            tokenizer.setSource(source);
            tokenizer.tokenize();

            PopcornParser parser = new PopcornParser(tokenizer.getDiagnostics(), tokenizer.getStream());

            // TODO: 10/02/2020 Do error reporting here!
            return parser.parse();
        } catch (Exception ex) {
            throw new PopcornException("An exception occurred during lexical/syntactic analysis");
        }
    }
}
