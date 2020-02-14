package com.popcorn.utils.diagnostics;

import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.utils.utilities.ConversionUtils;

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

    public void reportUndefinedUnaryOperator(Object object, ConversionUtils.DataType dataType) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Unary operator {0} is not defined for type {1}";

        report(type, message, object, dataType);
    }

    public void reportUndefinedBinaryOperator(Object object, ConversionUtils.DataType leftType, ConversionUtils.DataType rightType) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Binary operator {0} is not defined for types {1} and {2}";

        report(type, message, object, leftType, rightType);
    }

    public void reportAlreadyDefinedIdentifier(String identifier) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "{0} has already been defined";

        report(type, message, identifier);
    }

    public void reportUndefinedIdentifier(String identifier) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Variable {0} does not exist";

        report(type, message, identifier);
    }

    public void reportIncorrectType(String identifier, ConversionUtils.DataType expected, ConversionUtils.DataType actual) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Can't assign type {0} to variable {1}, expected expression of type {2}";

        report(type, message, actual, identifier, expected);
    }

    public void reportIncompatibleTypes(ConversionUtils.DataType leftType, ConversionUtils.DataType rightType) {
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Incompatible types {0} and {1}";

        report(type, message, leftType, rightType);
    }

    public void reportDivisionByZero(){
        Diagnostic.DiagnosticType type = Diagnostic.DiagnosticType.ERROR;
        String message = "Illegal Arithmetic: Division by 0";

        report(type, message);
    }
}
