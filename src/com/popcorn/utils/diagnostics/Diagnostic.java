package com.popcorn.utils.diagnostics;

public class Diagnostic {

    public enum DiagnosticType {
        ERROR,
        WARNING
    }

    private String message;
    private DiagnosticType type;

    public Diagnostic(String message, DiagnosticType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public DiagnosticType getType() {
        return type;
    }

}
