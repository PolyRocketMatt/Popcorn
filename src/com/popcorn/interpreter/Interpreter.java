package com.popcorn.interpreter;

import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.expressions.*;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.rules.EqualityRules;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.text.MessageFormat;
import java.util.HashMap;

public class Interpreter {

    private HashMap<VariableSymbol, LiteralValue> variables;

    public Interpreter() {
        this.variables = new HashMap<>();
    }

    public HashMap<VariableSymbol, LiteralValue> getVariables() {
        return variables;
    }

    public LiteralValue evaluate(BoundNode node) throws Exception {
        return evaluateExpression(node);
    }

    private LiteralValue evaluateExpression(BoundNode node) throws Exception {
        if (node  instanceof BoundLiteralExpressionNode)
            return ((BoundLiteralExpressionNode) node).getValue();

        if (node instanceof BoundAssignmentExpressionNode) {
            if (!variables.containsKey(((BoundAssignmentExpressionNode) node).getVariable())) {
                throw new Exception(MessageFormat.format("Variable {0} is not defined", ((BoundAssignmentExpressionNode) node).getVariable().getName()));
            }

            LiteralValue evaluatedAssignment = evaluateExpression(((BoundAssignmentExpressionNode) node).getExpression());

            variables.replace(((BoundAssignmentExpressionNode) node).getVariable(), evaluatedAssignment);

            return evaluatedAssignment;
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
