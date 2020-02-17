package com.popcorn.interpreter;

import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.expressions.*;
import com.popcorn.compiler.binding.node.statements.BoundElseIfStatementNode;
import com.popcorn.compiler.binding.node.statements.BoundElseStatementNode;
import com.popcorn.compiler.binding.node.statements.BoundIfStatementNode;
import com.popcorn.compiler.binding.node.statements.BoundPrintStatementNode;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.enums.BoundBinaryOperatorKind;
import com.popcorn.utils.enums.BoundUnaryOperatorKind;
import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.rules.EqualityRules;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.util.HashMap;

public class Interpreter {

    private DiagnosticsBag diagnostics;
    private HashMap<VariableSymbol, LiteralValue> variables;

    public Interpreter() {
        diagnostics = new DiagnosticsBag();
        variables = new HashMap<>();
    }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public HashMap<VariableSymbol, LiteralValue> getVariables() {
        return variables;
    }

    public LiteralValue evaluate(BoundNode node) throws PopcornException {
        return evaluateExpression(node);
    }

    private LiteralValue evaluateExpression(BoundNode node) throws PopcornException {
        switch (node.getKind()) {
            case LITERAL_EXPRESSION:
                return ((BoundLiteralExpressionNode) node).getValue();

            case NAME_EXPRESSION:
                return variables.get(((BoundNameExpressionNode) node).getVariable());

            case ASSIGNMENT_EXPRESSION:
                if (!variables.containsKey(((BoundAssignmentExpressionNode) node).getVariable())) {
                    diagnostics.reportUndefinedIdentifier(((BoundAssignmentExpressionNode) node).getVariable().getName());

                    return new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null);
                }

                LiteralValue evaluatedAssignment = evaluateExpression(((BoundAssignmentExpressionNode) node).getExpression());

                variables.replace(((BoundAssignmentExpressionNode) node).getVariable(), evaluatedAssignment);

                return evaluatedAssignment;

            case UNARY_EXPRESSION:
                LiteralValue evaluatedOperand = evaluateExpression(((BoundUnaryExpressionNode) node).getOperand());
                ConversionUtils.DataType dataType = evaluatedOperand.getType();
                BoundUnaryOperatorKind unaryOperatorKind = ((BoundUnaryExpressionNode) node).getOperator().getOperatorKind();

                switch (dataType) {
                    case INT:
                        switch (unaryOperatorKind) {
                            case IDENTITY:
                                return new LiteralValue(ConversionUtils.DataType.INT, ValueType.INT, evaluatedOperand.getValue());

                            case NEGATION:
                                return new LiteralValue(ConversionUtils.DataType.INT, ValueType.INT, -(int) evaluatedOperand.getValue());

                            default:
                            case LOGICAL_NEGATION:
                                diagnostics.reportUndefinedUnaryOperator(unaryOperatorKind.toString(), dataType);
                        }
                        break;

                    case FLOAT:
                        switch (unaryOperatorKind) {
                            case IDENTITY:
                                return new LiteralValue(ConversionUtils.DataType.FLOAT, ValueType.FLOAT, evaluatedOperand.getValue());

                            case NEGATION:
                                return new LiteralValue(ConversionUtils.DataType.FLOAT, ValueType.FLOAT, -(float) evaluatedOperand.getValue());

                            default:
                            case LOGICAL_NEGATION:
                                diagnostics.reportUndefinedUnaryOperator(unaryOperatorKind.toString(), dataType);
                                break;
                        }
                        throw new PopcornException("Could not perform unary operation {0} on type {1}", unaryOperatorKind, dataType);

                    case BOOL:
                        switch (unaryOperatorKind) {
                            default:
                            case IDENTITY:
                            case NEGATION:
                                diagnostics.reportUndefinedUnaryOperator(unaryOperatorKind.toString(), dataType);
                                break;

                            case LOGICAL_NEGATION:
                                return new LiteralValue(ConversionUtils.DataType.BOOL, ValueType.BOOL, !(boolean) evaluatedOperand.getValue());
                        }
                        throw new PopcornException("Could not perform unary operation {0} on type {1}", unaryOperatorKind, dataType);

                    case STRING:
                    default:
                    case NOT_DEFINED:
                        diagnostics.reportUndefinedUnaryOperator(unaryOperatorKind.toString(), dataType);
                        break;
                }

            case BINARY_EXPRESSION:
                LiteralValue evaluatedLeft = evaluateExpression(((BoundBinaryExpressionNode) node).getLeft());
                LiteralValue evaluatedRight = evaluateExpression(((BoundBinaryExpressionNode) node).getRight());
                ConversionUtils.DataType leftType = evaluatedLeft.getType();
                ConversionUtils.DataType rightType = evaluatedRight.getType();
                BoundBinaryOperatorKind binaryOperatorKind = ((BoundBinaryExpressionNode) node).getOperator().getOperatorKind();

                switch (binaryOperatorKind) {
                    case ADDITION:
                        switch (leftType) {
                            case INT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.INT,
                                                ValueType.INT,
                                                (int) evaluatedLeft.getValue() + (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (int) evaluatedLeft.getValue() + (float) evaluatedRight.getValue()
                                        );

                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            case FLOAT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() + (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() + (float) evaluatedRight.getValue()
                                        );
                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            case STRING:
                                if (rightType != ConversionUtils.DataType.NOT_DEFINED) {
                                    return new LiteralValue(
                                            ConversionUtils.DataType.STRING,
                                            ValueType.STRING,
                                            evaluatedLeft.toString() + evaluatedRight.toString());
                                } else {
                                    diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                    break;
                                }

                            default:
                                diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                break;
                        }
                        break;

                    case SUBTRACTION:
                        switch (leftType) {
                            case INT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.INT,
                                                ValueType.INT,
                                                (int) evaluatedLeft.getValue() - (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (int) evaluatedLeft.getValue() - (float) evaluatedRight.getValue()
                                        );

                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            case FLOAT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() - (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() - (float) evaluatedRight.getValue()
                                        );
                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            default:
                                diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                break;
                        }
                        break;

                    case MULTIPLICATION:
                        switch (leftType) {
                            case INT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.INT,
                                                ValueType.INT,
                                                (int) evaluatedLeft.getValue() * (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (int) evaluatedLeft.getValue() * (float) evaluatedRight.getValue()
                                        );

                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            case FLOAT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() * (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() * (float) evaluatedRight.getValue()
                                        );
                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            default:
                                diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                break;
                        }
                        break;

                    case DIVISION:
                        switch (leftType) {
                            case INT:
                                switch (rightType) {
                                    case INT:
                                        if ((int) evaluatedRight.getValue() == 0) {
                                            diagnostics.reportDivisionByZero();
                                            break;
                                        } else {
                                            return new LiteralValue(
                                                    ConversionUtils.DataType.INT,
                                                    ValueType.INT,
                                                    (int) evaluatedLeft.getValue() / (int) evaluatedRight.getValue()
                                            );
                                        }

                                    case FLOAT:
                                        if ((float) evaluatedRight.getValue() == 0f) {
                                            diagnostics.reportDivisionByZero();
                                            break;
                                        } else {
                                            return new LiteralValue(
                                                    ConversionUtils.DataType.FLOAT,
                                                    ValueType.FLOAT,
                                                    (int) evaluatedLeft.getValue() / (float) evaluatedRight.getValue()
                                            );
                                        }

                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            case FLOAT:
                                switch (rightType) {
                                    case INT:
                                        if ((int) evaluatedRight.getValue() == 0) {
                                            diagnostics.reportDivisionByZero();
                                            break;
                                        } else {
                                            return new LiteralValue(
                                                    ConversionUtils.DataType.FLOAT,
                                                    ValueType.FLOAT,
                                                    (int) evaluatedLeft.getValue() / (int) evaluatedRight.getValue()
                                            );
                                        }

                                    case FLOAT:
                                        if ((float) evaluatedRight.getValue() == 0f) {
                                            diagnostics.reportDivisionByZero();
                                            break;
                                        } else {
                                            return new LiteralValue(
                                                    ConversionUtils.DataType.FLOAT,
                                                    ValueType.FLOAT,
                                                    (int) evaluatedLeft.getValue() / (int) evaluatedRight.getValue()
                                            );
                                        }
                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            default:
                                diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                break;
                        }
                        break;

                    case MODULO:
                        switch (leftType) {
                            case INT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.INT,
                                                ValueType.INT,
                                                (int) evaluatedLeft.getValue() % (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (int) evaluatedLeft.getValue() % (float) evaluatedRight.getValue()
                                        );

                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            case FLOAT:
                                switch (rightType) {
                                    case INT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() % (int) evaluatedRight.getValue()
                                        );

                                    case FLOAT:
                                        return new LiteralValue(
                                                ConversionUtils.DataType.FLOAT,
                                                ValueType.FLOAT,
                                                (float) evaluatedLeft.getValue() % (float) evaluatedRight.getValue()
                                        );
                                    default:
                                        diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                        break;
                                }
                                break;

                            default:
                                diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                                break;
                        }
                        break;

                    case LOGICAL_AND:
                        if (leftType == ConversionUtils.DataType.BOOL && rightType == ConversionUtils.DataType.BOOL) {
                            return new LiteralValue(
                                    ConversionUtils.DataType.BOOL,
                                    ValueType.BOOL,
                                    (boolean) evaluatedLeft.getValue() && (boolean) evaluatedRight.getValue()
                            );
                        } else {
                            diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                            break;
                        }

                    case LOGICAL_OR:
                        if (leftType == ConversionUtils.DataType.BOOL && rightType == ConversionUtils.DataType.BOOL) {
                            return new LiteralValue(
                                    ConversionUtils.DataType.BOOL,
                                    ValueType.BOOL,
                                    (boolean) evaluatedLeft.getValue() || (boolean) evaluatedRight.getValue()
                            );
                        } else {
                            diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                            break;
                        }

                    case LOGICAL_EQUALS:
                        if (leftType == rightType) {
                            return new LiteralValue(
                                    ConversionUtils.DataType.BOOL,
                                    ValueType.BOOL,
                                    EqualityRules.isEqual(evaluatedLeft, evaluatedRight)
                            );
                        } else {
                            diagnostics.reportIncompatibleTypes(leftType, rightType);
                            break;
                        }

                    case LOGICAL_NOT_EQUALS:
                        if (leftType == rightType) {
                            return new LiteralValue(
                                    ConversionUtils.DataType.BOOL,
                                    ValueType.BOOL,
                                    !EqualityRules.isEqual(evaluatedLeft, evaluatedRight)
                            );
                        } else {
                            diagnostics.reportIncompatibleTypes(leftType, rightType);
                            break;
                        }

                    default:
                        break;
                }

                diagnostics.reportUndefinedBinaryOperator(binaryOperatorKind.toString(), leftType, rightType);
                break;

            case IF_STATEMENT:
                LiteralValue evaluatedComparison = evaluateExpression(((BoundIfStatementNode) node).getBoundExpression());

                if (evaluatedComparison.getType() != ConversionUtils.DataType.BOOL) {
                    diagnostics.reportComparisonNotBoolean(evaluatedComparison.getType());
                } else {
                    if ((boolean) evaluatedComparison.getValue()) {
                        for (BoundNode bodyNode : ((BoundIfStatementNode) node).getBoundNodes())
                            evaluate(bodyNode);
                    } else {
                        boolean hasFoundElse = false;

                        System.out.println("Yey");

                        if (!((BoundIfStatementNode) node).getElseIfStatementNodes().isEmpty()) {
                            for (BoundElseIfStatementNode elseIfNode : ((BoundIfStatementNode) node).getElseIfStatementNodes()) {
                                LiteralValue evaluatedElseComparison = evaluateExpression(elseIfNode.getBoundExpression());

                                if (evaluatedElseComparison.getType() != ConversionUtils.DataType.BOOL) {
                                    diagnostics.reportComparisonNotBoolean(evaluatedComparison.getType());
                                } else {
                                    if ((boolean) evaluatedElseComparison.getValue()) {
                                        hasFoundElse = true;
                                        for (BoundNode bodyNode : elseIfNode.getBoundNodes())
                                            evaluate(bodyNode);
                                        break;
                                    }
                                }
                            }
                        }

                        if (!hasFoundElse) {
                            if (((BoundIfStatementNode) node).getBoundElseStatement() != null) {
                                BoundElseStatementNode elseStatement = ((BoundIfStatementNode) node).getBoundElseStatement();

                                for (BoundNode bodyNode : elseStatement.getBoundNodes())
                                    evaluate(bodyNode);
                            }
                        }
                    }
                }

                return evaluatedComparison;

            case PRINT_STATEMENT:
                LiteralValue evaluatePrintExpression = evaluateExpression(((BoundPrintStatementNode) node).getBoundExpression());
                String value = evaluatePrintExpression.getValue().toString();
                String representedValue = value.substring(1, value.length() - 1);
                System.out.println(representedValue);

                return evaluatePrintExpression;

            case SKIP:
                break;

            default:
                diagnostics.reportUndefinedInstruction(node.getKind());
        }

        return new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null);
    }
}
