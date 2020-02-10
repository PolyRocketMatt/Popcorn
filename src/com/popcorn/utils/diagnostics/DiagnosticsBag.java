package com.popcorn.utils.diagnostics;

import com.popcorn.compiler.lexical.TokenType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticsBag {

    private List<Diagnostic> diagnostics;

    public DiagnosticsBag() {
        this.diagnostics = new ArrayList<>();
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public void addBag(DiagnosticsBag bag) {
        diagnostics.addAll(bag.diagnostics);
    }

    private void report(Diagnostic.DiagnosticType type, String message, Object...objects) {
        diagnostics.add(new Diagnostic(MessageFormat.format(message, objects), type));
    }

    public void reportInvalidInput(String input, int line, int column) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Invalid input \"{0}\" at line {1}, column {2}";

        report(type, message, line, column);
    }

    public void reportUnexpectedToken(TokenType unexpected, String expected) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Unexpected token {0}, expected {1}";

        report(type, message, unexpected, expected);
    }

    public void reportUnexpectedLiteralException(TokenType tokenType) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Unexpected type for literal {0}";

        report(type, message, tokenType);
    }
}