package com.popcorn.compiler.binding;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.BoundSkipNode;
import com.popcorn.compiler.binding.node.BoundStatementNode;
import com.popcorn.compiler.binding.node.expressions.*;
import com.popcorn.compiler.binding.node.statements.*;
import com.popcorn.compiler.binding.operators.BoundBinaryOperator;
import com.popcorn.compiler.binding.operators.BoundUnaryOperator;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.expressions.*;
import com.popcorn.compiler.node.statements.*;
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

    public BoundNode bindNode(Node node) throws PopcornException {
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
            case IF_STATEMENT_NODE:
                return bindIfStatement((IfStatementNode) node);
            case ELSE_IF_STATEMENT_NODE:
                return bindElseIfStatement((ElseIfStatementNode) node);
            case ELSE_STATEMENT_NODE:
                return bindElseStatement((ElseStatementNode) node);
            case PRINT_STATEMENT_NODE:
                return bindPrintStatement((PrintStatementNode) node);
            case SKIP_NODE:
                return new BoundSkipNode();
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
            BoundExpressionNode boundExpression = (BoundExpressionNode) bindNode(node.getExpression());

            if (variables.stream().anyMatch(variableSymbol -> variableSymbol.getName().equals(name))) {
                diagnostics.reportAlreadyDefinedIdentifier(name);

                return new BoundLiteralExpressionNode(new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
            }

            VariableSymbol symbolization = new VariableSymbol(name, type);

            variables.add(symbolization);

            return new BoundAssignmentExpressionNode(symbolization, boundExpression);
        } else {
            BoundExpressionNode boundExpression = (BoundExpressionNode) bindNode(node.getExpression());
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
        return (BoundExpressionNode) bindNode(node.getExpression());
    }

    private BoundExpressionNode bindUnaryExpression(UnaryExpressionNode node) throws PopcornException {
        BoundExpressionNode boundOperand = (BoundExpressionNode) bindNode(node.getOperand());
        BoundUnaryOperator boundOperator = BoundUnaryOperator.bind(node.getOperatorToken().getType(), boundOperand.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedUnaryOperator(node.getOperatorToken().getValue(), boundOperand.getType());
            return boundOperand;
        }

        return new BoundUnaryExpressionNode(boundOperator, boundOperand);
    }

    private BoundExpressionNode bindBinaryExpression(BinaryExpressionNode node) throws PopcornException {
        BoundExpressionNode boundLeft = (BoundExpressionNode) bindNode(node.getLeft());
        BoundExpressionNode boundRight = (BoundExpressionNode) bindNode(node.getRight());
        BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(node.getOperatorToken().getType(), boundLeft.getType(), boundRight.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedBinaryOperator(node.getOperatorToken().getValue(), boundLeft.getType(), boundRight.getType());
            return boundLeft;
        }

        return new BoundBinaryExpressionNode(boundOperator, boundLeft, boundRight);
    }

    private BoundStatementNode bindIfStatement(IfStatementNode node) throws PopcornException {
        BoundExpressionNode boundExpression = (BoundExpressionNode) bindNode(node.getExpression());
        ArrayList<BoundNode> boundNodes = new ArrayList<>();
        ArrayList<BoundElseIfStatementNode> boundElseIfStatementNodes = new ArrayList<>();
        BoundElseStatementNode boundElseStatement = null;

        for (Node bodyNode : node.getBody())
            boundNodes.add(bindNode(bodyNode));
        for (Node elseIfNode : node.getElseIfStatementNodes())
            boundElseIfStatementNodes.add((BoundElseIfStatementNode) bindNode(elseIfNode));
        if (node.getElseStatementNode() != null)
            boundElseStatement = (BoundElseStatementNode) bindNode(node.getElseStatementNode());

        return new BoundIfStatementNode(boundExpression, boundNodes, boundElseIfStatementNodes, boundElseStatement);
    }

    private BoundStatementNode bindElseIfStatement(ElseIfStatementNode node) throws PopcornException {
        BoundExpressionNode boundExpression = (BoundExpressionNode) bindNode(node.getExpression());
        ArrayList<BoundNode> boundNodes = new ArrayList<>();

        for (Node bodyNode : node.getBody())
            boundNodes.add(bindNode(bodyNode));
        return new BoundElseIfStatementNode(boundExpression, boundNodes);
    }

    private BoundStatementNode bindElseStatement(ElseStatementNode node) throws PopcornException {
        ArrayList<BoundNode> boundNodes = new ArrayList<>();

        for (Node bodyNode : node.getBody())
            boundNodes.add(bindNode(bodyNode));
        return new BoundElseStatementNode(boundNodes);
    }

    private BoundStatementNode bindPrintStatement(PrintStatementNode node) throws PopcornException {
        BoundExpressionNode boundExpression = (BoundExpressionNode) bindNode(node.getExpression());

        return new BoundPrintStatementNode(boundExpression);
    }
}
