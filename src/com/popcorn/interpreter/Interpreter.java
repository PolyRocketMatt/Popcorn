package com.popcorn.interpreter;

import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.expressions.*;
import com.popcorn.compiler.node.expressions.AssignmentExpressionNode;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.rules.EqualityRules;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {

    private BoundNode root;
    private HashMap<VariableSymbol, LiteralValue> variables;

    public Interpreter(BoundNode root, ArrayList<VariableSymbol> variableSymbols) {
        this.root = root;
        this.variables = new HashMap<>();

        for (VariableSymbol symbol : variableSymbols)
            variables.put(symbol, null);
    }

    public BoundNode getRoot() {
        return root;
    }

    public LiteralValue evaluate() throws Exception {
        return evaluateExpression(root);
    }

    private LiteralValue evaluateExpression(BoundNode node) throws Exception {
        if (node  instanceof BoundLiteralExpressionNode)
            return ((BoundLiteralExpressionNode) node).getValue();

        if (node instanceof BoundVariableExpressionNode)
            return variables.get(((BoundVariableExpressionNode) node).getVariable());

        if (node instanceof BoundAssignmentExpressionNode) {
            LiteralValue value = evaluateExpression(((BoundAssignmentExpressionNode) node).getExpression());

            variables.put(((BoundAssignmentExpressionNode) node).getVariable(), value);

            return value;
        }

        if (node instanceof BoundUnaryExpressionNode) {
            LiteralValue evaluatedOperand = evaluateExpression(((BoundUnaryExpressionNode) node).getOperand());

            switch (((BoundUnaryExpressionNode) node).getOperator().getOperatorKind()) {
                case IDENTITY:
                    return new LiteralValue(ConversionUtils.DataType.INT, (int) evaluatedOperand.getValue());
                case NEGATION:
                    return new LiteralValue(ConversionUtils.DataType.INT, -(int) evaluatedOperand.getValue());
                case LOGICAL_NEGATION:
                    return new LiteralValue(ConversionUtils.DataType.BOOL, !(boolean) evaluatedOperand.getValue());
                default:
                    throw new Exception(MessageFormat.format("Unexpected unary operator {0}", ((BoundUnaryExpressionNode) node).getOperator().getOperatorKind()));
            }
        }

        if (node instanceof BoundBinaryExpressionNode) {
            LiteralValue evaluatedLeft = evaluateExpression(((BoundBinaryExpressionNode) node).getLeft());
            LiteralValue evaluatedRight = evaluateExpression(((BoundBinaryExpressionNode) node).getRight());

            switch (((BoundBinaryExpressionNode) node).getOperator().getOperatorKind()) {
                case ADDITION:
                    return new LiteralValue(ConversionUtils.DataType.INT, (int) evaluatedLeft.getValue() + (int) evaluatedRight.getValue());
                case SUBTRACTION:
                    return new LiteralValue(ConversionUtils.DataType.INT, (int) evaluatedLeft.getValue() - (int) evaluatedRight.getValue());
                case MULTIPLICATION:
                    return new LiteralValue(ConversionUtils.DataType.INT, (int) evaluatedLeft.getValue() * (int) evaluatedRight.getValue());
                case DIVISION:
                    return new LiteralValue(ConversionUtils.DataType.INT, (int) evaluatedLeft.getValue() / (int) evaluatedRight.getValue());
                case MODULO:
                    return new LiteralValue(ConversionUtils.DataType.INT, (int) evaluatedLeft.getValue() % (int) evaluatedRight.getValue());
                case LOGICAL_AND:
                    return new LiteralValue(ConversionUtils.DataType.BOOL, (boolean) evaluatedLeft.getValue() && (boolean) evaluatedRight.getValue());
                case LOGICAL_OR:
                    return new LiteralValue(ConversionUtils.DataType.BOOL, (boolean) evaluatedLeft.getValue() || (boolean) evaluatedRight.getValue());
                case LOGICAL_EQUALS:
                    return new LiteralValue(ConversionUtils.DataType.BOOL, EqualityRules.isEqual(evaluatedLeft, evaluatedRight));
                case LOGICAL_NOT_EQUALS:
                    return new LiteralValue(ConversionUtils.DataType.BOOL, !EqualityRules.isEqual(evaluatedLeft, evaluatedRight));
            }
        }

        throw new Exception(MessageFormat.format("Unexpected node {0}", node.getKind()));
    }
}
