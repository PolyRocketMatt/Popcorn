package com.popcorn.interpreter;

import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.expressions.*;
import com.popcorn.compiler.binding.operators.BoundBinaryOperator;
import com.popcorn.compiler.binding.operators.BoundUnaryOperator;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.enums.BoundBinaryOperatorKind;
import com.popcorn.utils.enums.BoundUnaryOperatorKind;
import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.rules.EqualityRules;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.text.MessageFormat;
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

    public LiteralValue evaluate(BoundNode node) throws Exception {
        return evaluateExpression(node);
    }

    private LiteralValue evaluateExpression(BoundNode node) throws Exception {
        if (node  instanceof BoundLiteralExpressionNode)
            return ((BoundLiteralExpressionNode) node).getValue();

        if (node instanceof BoundNameExpressionNode)
            return variables.get(((BoundNameExpressionNode) node).getVariable());

        if (node instanceof BoundNullExpressionNode) {
            return ((BoundNullExpressionNode) node).getNullValue();
        }

        if (node instanceof BoundAssignmentExpressionNode) {
            if (!variables.containsKey(((BoundAssignmentExpressionNode) node).getVariable())) {
                diagnostics.reportUndefinedIdentifier(((BoundAssignmentExpressionNode) node).getVariable().getName());

                return new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null);
            }

            LiteralValue evaluatedAssignment = evaluateExpression(((BoundAssignmentExpressionNode) node).getExpression());

            variables.replace(((BoundAssignmentExpressionNode) node).getVariable(), evaluatedAssignment);

            return evaluatedAssignment;
        }

        if (node instanceof BoundUnaryExpressionNode) {
            LiteralValue evaluatedOperand = evaluateExpression(((BoundUnaryExpressionNode) node).getOperand());
            ConversionUtils.DataType dataType = evaluatedOperand.getType();
            BoundUnaryOperatorKind operatorKind = ((BoundUnaryExpressionNode) node).getOperator().getOperatorKind();

            switch (dataType) {
                case INT:
                    switch (operatorKind) {
                        case IDENTITY:
                            return new LiteralValue(ConversionUtils.DataType.INT, ValueType.INT, evaluatedOperand.getValue());

                        case NEGATION:
                            return new LiteralValue(ConversionUtils.DataType.INT, ValueType.INT, -(int) evaluatedOperand.getValue());

                        default:
                        case LOGICAL_NEGATION:
                            diagnostics.reportUndefinedUnaryOperator(operatorKind.toString(), dataType);
                    }
                    break;

                case FLOAT:
                    switch (operatorKind) {
                        case IDENTITY:
                            return new LiteralValue(ConversionUtils.DataType.FLOAT, ValueType.FLOAT, (float) evaluatedOperand.getValue());

                        case NEGATION:
                            return new LiteralValue(ConversionUtils.DataType.FLOAT, ValueType.FLOAT, -(float) evaluatedOperand.getValue());

                        default:
                        case LOGICAL_NEGATION:
                            diagnostics.reportUndefinedUnaryOperator(operatorKind.toString(), dataType);
                            break;
                    }
                    throw new Exception("Internal error occurred");

                case BOOL:
                    switch (operatorKind) {
                        default:
                        case IDENTITY:
                        case NEGATION:
                            diagnostics.reportUndefinedUnaryOperator(operatorKind.toString(), dataType);
                            break;

                        case LOGICAL_NEGATION:
                            return new LiteralValue(ConversionUtils.DataType.BOOL, ValueType.BOOL, !(boolean) evaluatedOperand.getValue());
                    }
                    throw new Exception("Internal error occurred");

                case STRING:
                default:
                case NOT_DEFINED:
                    diagnostics.reportUndefinedUnaryOperator(operatorKind.toString(), dataType);
                    break;
            }
        }

        if (node instanceof BoundBinaryExpressionNode) {
            LiteralValue evaluatedLeft = evaluateExpression(((BoundBinaryExpressionNode) node).getLeft());
            LiteralValue evaluatedRight = evaluateExpression(((BoundBinaryExpressionNode) node).getRight());
            ConversionUtils.DataType leftType = evaluatedLeft.getType();
            ConversionUtils.DataType rightType = evaluatedRight.getType();
            BoundBinaryOperatorKind operatorKind = ((BoundBinaryExpressionNode) node).getOperator().getOperatorKind();

            switch (operatorKind) {
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
                                break;
                            }

                        default:
                            diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
                                    break;
                            }
                            break;

                        default:
                            diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
                                    break;
                            }
                            break;

                        default:
                            diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
                                    break;
                            }
                            break;

                        default:
                            diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                                    diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
                                    break;
                            }
                            break;

                        default:
                            diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                        diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
                        diagnostics.reportUndefinedBinaryOperator(operatorKind.toString(), leftType, rightType);
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
        }

        return new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null);
    }
}
