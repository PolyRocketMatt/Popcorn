package com.popcorn.compiler.binding.operators;

import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.utils.enums.BoundBinaryOperatorKind;
import com.popcorn.utils.utilities.ConversionUtils;

public class BoundBinaryOperator implements Operator {

    private TokenType tokenType;
    private BoundBinaryOperatorKind operatorKind;
    private ConversionUtils.DataType leftType;
    private ConversionUtils.DataType rightType;
    private ConversionUtils.DataType type;

    public ConversionUtils.DataType getType() {
        return type;
    }

    private BoundBinaryOperator(TokenType tokenType, BoundBinaryOperatorKind operatorKind,
                                ConversionUtils.DataType leftType, ConversionUtils.DataType rightType, ConversionUtils.DataType type) {
        this.tokenType = tokenType;
        this.operatorKind = operatorKind;
        this.leftType = leftType;
        this.rightType = rightType;
        this.type = type;
    }

    private BoundBinaryOperator(TokenType tokenType, BoundBinaryOperatorKind operatorKind,
                               ConversionUtils.DataType operandType, ConversionUtils.DataType type) {
        this.tokenType = tokenType;
        this.operatorKind = operatorKind;
        this.leftType = operandType;
        this.rightType = operandType;
        this.type = type;
    }

    private BoundBinaryOperator(TokenType tokenType, BoundBinaryOperatorKind operatorKind, ConversionUtils.DataType type) {
        this.tokenType = tokenType;
        this.operatorKind = operatorKind;
        this.leftType = type;
        this.rightType = type;
        this.type = type;
    }

    private static BoundBinaryOperator[] operators = new BoundBinaryOperator[]
            {
                    new BoundBinaryOperator(TokenType.PLUS, BoundBinaryOperatorKind.ADDITION, ConversionUtils.DataType.INT),
                    new BoundBinaryOperator(TokenType.MINUS, BoundBinaryOperatorKind.SUBTRACTION, ConversionUtils.DataType.INT),
                    new BoundBinaryOperator(TokenType.ASTERISK, BoundBinaryOperatorKind.MULTIPLICATION, ConversionUtils.DataType.INT),
                    new BoundBinaryOperator(TokenType.F_SLASH, BoundBinaryOperatorKind.DIVISION, ConversionUtils.DataType.INT),
                    new BoundBinaryOperator(TokenType.MODULO, BoundBinaryOperatorKind.MODULO, ConversionUtils.DataType.INT),

                    new BoundBinaryOperator(TokenType.PLUS, BoundBinaryOperatorKind.ADDITION, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.MINUS, BoundBinaryOperatorKind.SUBTRACTION, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.ASTERISK, BoundBinaryOperatorKind.MULTIPLICATION, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.F_SLASH, BoundBinaryOperatorKind.DIVISION, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.MODULO, BoundBinaryOperatorKind.MODULO, ConversionUtils.DataType.FLOAT),

                    // TODO: 10/02/2020 Check these operations
                    new BoundBinaryOperator(TokenType.PLUS, BoundBinaryOperatorKind.ADDITION, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.MINUS, BoundBinaryOperatorKind.SUBTRACTION, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.ASTERISK, BoundBinaryOperatorKind.MULTIPLICATION, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.F_SLASH, BoundBinaryOperatorKind.DIVISION, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.MODULO, BoundBinaryOperatorKind.MODULO, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.FLOAT),

                    new BoundBinaryOperator(TokenType.PLUS, BoundBinaryOperatorKind.ADDITION, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.MINUS, BoundBinaryOperatorKind.SUBTRACTION, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.ASTERISK, BoundBinaryOperatorKind.MULTIPLICATION, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.F_SLASH, BoundBinaryOperatorKind.DIVISION, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT),
                    new BoundBinaryOperator(TokenType.MODULO, BoundBinaryOperatorKind.MODULO, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT),

                    // TODO: 10/02/2020 Check these operations
                    new BoundBinaryOperator(TokenType.DOUBLE_EQUALS, BoundBinaryOperatorKind.LOGICAL_EQUALS, ConversionUtils.DataType.INT, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.DOUBLE_EQUALS, BoundBinaryOperatorKind.LOGICAL_EQUALS, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.DOUBLE_EQUALS, BoundBinaryOperatorKind.LOGICAL_EQUALS, ConversionUtils.DataType.STRING, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.DOUBLE_EQUALS, BoundBinaryOperatorKind.LOGICAL_EQUALS, ConversionUtils.DataType.BOOL, ConversionUtils.DataType.BOOL),

                    new BoundBinaryOperator(TokenType.NOT_EQUALS, BoundBinaryOperatorKind.LOGICAL_NOT_EQUALS, ConversionUtils.DataType.INT, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.NOT_EQUALS, BoundBinaryOperatorKind.LOGICAL_NOT_EQUALS, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.NOT_EQUALS, BoundBinaryOperatorKind.LOGICAL_NOT_EQUALS, ConversionUtils.DataType.STRING, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.NOT_EQUALS, BoundBinaryOperatorKind.LOGICAL_NOT_EQUALS, ConversionUtils.DataType.BOOL, ConversionUtils.DataType.BOOL),

                    new BoundBinaryOperator(TokenType.DOUBLE_EQUALS, BoundBinaryOperatorKind.LOGICAL_EQUALS, ConversionUtils.DataType.INT, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.DOUBLE_EQUALS, BoundBinaryOperatorKind.LOGICAL_EQUALS, ConversionUtils.DataType.FLOAT, ConversionUtils.DataType.INT, ConversionUtils.DataType.BOOL),

                    new BoundBinaryOperator(TokenType.DOUBLE_AMPERSAND, BoundBinaryOperatorKind.LOGICAL_AND, ConversionUtils.DataType.BOOL),
                    new BoundBinaryOperator(TokenType.DOUBLE_PIPE, BoundBinaryOperatorKind.LOGICAL_OR, ConversionUtils.DataType.BOOL),
            };

    public static BoundBinaryOperator bind(TokenType tokenType, ConversionUtils.DataType leftType, ConversionUtils.DataType rightType) {
        for (BoundBinaryOperator operator : operators) {
            if (operator.tokenType == tokenType &&
                    operator.leftType == leftType &&
                    operator.rightType == rightType)
                return operator;
        }

        return null;
    }
}
