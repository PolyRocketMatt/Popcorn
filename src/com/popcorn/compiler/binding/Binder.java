package com.popcorn.compiler.binding;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.node.expressions.*;
import com.popcorn.compiler.binding.operators.BoundBinaryOperator;
import com.popcorn.compiler.binding.operators.BoundUnaryOperator;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.expressions.*;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.util.ArrayList;

public class Binder {

    private DiagnosticsBag diagnostics;
    private ArrayList<VariableSymbol> variables;

    public Binder() {
        diagnostics = new DiagnosticsBag();
        variables = new ArrayList<>();
    }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public ArrayList<VariableSymbol> getVariables() {
        return variables;
    }

    public BoundExpressionNode bindExpression(ExpressionNode node) throws PopcornException {
        switch (node.getNodeType()) {
            case LITERAL_NODE:
                return bindLiteralExpression((LiteralExpressionNode) node);
            case NAME_NODE:
                return bindNameExpression((NameExpressionNode) node);
            case ASSIGNMENT_NODE:
                return bindAssignmentExpression((AssignmentExpressionNode) node);
            case PARENTHESIZED_EXPRESSION_NODE:
                return bindParenthesizedExpression((ParenthesizedExpression) node);
            case UNARY_OPERATOR_NODE:
                return bindUnaryExpression((UnaryExpressionNode) node);
            case BINARY_OPERATOR_NODE:
                return bindBinaryExpression((BinaryExpressionNode) node);
            default:
                throw new PopcornException("Unexpected syntax {0}", node.getNodeType());
        }
    }

    private BoundExpressionNode bindLiteralExpression(LiteralExpressionNode node) {
        return new BoundLiteralExpressionNode(node.getValue());
    }

    private BoundExpressionNode bindNameExpression(NameExpressionNode node) {
        String name = (String) node.getIdentifierToken().getValue();
        VariableSymbol symbolization = variables.stream()
                .filter(variableSymbol -> variableSymbol.getName().equals(name)).findFirst().orElse(null);

        if (symbolization == null) {
            diagnostics.reportUndefinedIdentifier(name);

            return new BoundLiteralExpressionNode(new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
        }

        return new BoundNameExpressionNode(symbolization);
    }

    private BoundExpressionNode bindAssignmentExpression(AssignmentExpressionNode node) throws PopcornException {
        String name = (String) node.getIdentifierToken().getValue();
        if (node.getType() != ConversionUtils.DataType.NOT_DEFINED) {
            ConversionUtils.DataType type = node.getType();
            BoundExpressionNode boundExpression = bindExpression(node.getExpression());

            if (variables.stream().anyMatch(variableSymbol -> variableSymbol.getName().equals(name))) {
                diagnostics.reportAlreadyDefinedIdentifier(name);

                return new BoundLiteralExpressionNode(new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
            }

            VariableSymbol symbolization = new VariableSymbol(name, type);

            variables.add(symbolization);

            return new BoundAssignmentExpressionNode(symbolization, boundExpression);
        } else {
            BoundExpressionNode boundExpression = bindExpression(node.getExpression());
            VariableSymbol symbolization = variables.stream()
                    .filter(variableSymbol -> variableSymbol.getName().equals(name)).findFirst().orElse(null);

            if (symbolization == null) {
                diagnostics.reportUndefinedIdentifier(name);

                return new BoundLiteralExpressionNode(new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
            }

            if (boundExpression.getType() != symbolization.getType()) {
                diagnostics.reportIncorrectType(name, symbolization.getType(), boundExpression.getType());

                return new BoundLiteralExpressionNode(new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
            }

            return new BoundAssignmentExpressionNode(symbolization, boundExpression);
        }
    }

    private BoundExpressionNode bindParenthesizedExpression(ParenthesizedExpression node) throws PopcornException {
        return bindExpression(node.getExpression());
    }

    private BoundExpressionNode bindUnaryExpression(UnaryExpressionNode node) throws PopcornException {
        BoundExpressionNode boundOperand = bindExpression(node.getOperand());
        BoundUnaryOperator boundOperator = BoundUnaryOperator.bind(node.getOperatorToken().getType(), boundOperand.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedUnaryOperator(node.getOperatorToken().getValue(), boundOperand.getType());
            return boundOperand;
        }

        return new BoundUnaryExpressionNode(boundOperator, boundOperand);
    }

    private BoundExpressionNode bindBinaryExpression(BinaryExpressionNode node) throws PopcornException {
        BoundExpressionNode boundLeft = bindExpression(node.getLeft());
        BoundExpressionNode boundRight = bindExpression(node.getRight());
        BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(node.getOperatorToken().getType(), boundLeft.getType(), boundRight.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedBinaryOperator(node.getOperatorToken().getValue(), boundLeft.getType(), boundRight.getType());
            return boundLeft;
        }

        return new BoundBinaryExpressionNode(boundOperator, boundLeft, boundRight);
    }
}
