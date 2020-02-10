package com.popcorn.compiler.binding.operators;

import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.utils.enums.BoundUnaryOperatorKind;
import com.popcorn.utils.utilities.ConversionUtils;

public class BoundUnaryOperator implements Operator {

    private TokenType tokenType;
    private BoundUnaryOperatorKind operatorKind;
    private ConversionUtils.DataType operandType;
    private ConversionUtils.DataType type;

    public ConversionUtils.DataType getType() {
        return type;
    }

    private BoundUnaryOperator(TokenType tokenType, BoundUnaryOperatorKind operatorKind, ConversionUtils.DataType operandType, ConversionUtils.DataType type) {
        this.tokenType = tokenType;
        this.operatorKind = operatorKind;
        this.operandType = operandType;
        this.type = type;
    }

    private BoundUnaryOperator(TokenType tokenType, BoundUnaryOperatorKind operatorKind, ConversionUtils.DataType operandType) {
        this.tokenType = tokenType;
        this.operatorKind = operatorKind;
        this.operandType = operandType;
        this.type = operandType;
    }

    private static BoundUnaryOperator[] operators = new BoundUnaryOperator[]
            {
                    new BoundUnaryOperator(TokenType.EXCLAMATION, BoundUnaryOperatorKind.LOGICAL_NEGATION, ConversionUtils.DataType.BOOL),

                    new BoundUnaryOperator(TokenType.PLUS, BoundUnaryOperatorKind.IDENTITY, ConversionUtils.DataType.INT),
                    new BoundUnaryOperator(TokenType.MINUS, BoundUnaryOperatorKind.NEGATION, ConversionUtils.DataType.INT),

                    new BoundUnaryOperator(TokenType.PLUS, BoundUnaryOperatorKind.IDENTITY, ConversionUtils.DataType.FLOAT),
                    new BoundUnaryOperator(TokenType.MINUS, BoundUnaryOperatorKind.NEGATION, ConversionUtils.DataType.FLOAT),
            };

    public static BoundUnaryOperator bind(TokenType type, ConversionUtils.DataType operandType) {
        for (BoundUnaryOperator operator : operators) {
            if (operator.tokenType == type && operator.operandType == operandType)
                return operator;
        }

        return null;
    }
}
