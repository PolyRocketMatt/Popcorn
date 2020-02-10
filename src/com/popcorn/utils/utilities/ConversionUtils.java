package com.popcorn.utils.utilities;

import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.utils.enums.BinaryOperatorType;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class ConversionUtils {

    public enum DataType {
        INT,
        FLOAT,
        BOOL,
        STRING
    }

    private static final TokenType[] literals = { TokenType.INT_LITERAL, TokenType.FLOAT_LITERAL, TokenType.BOOL_LITERAL, TokenType.STRING_LITERAL };
    private static final TokenType[] types = { TokenType.VOID, TokenType.INT, TokenType.FLOAT_LITERAL, TokenType.BOOL_LITERAL, TokenType.STRING_LITERAL };
    private static final TokenType[] unaryOperators = { TokenType.PLUS, TokenType.MINUS };

    public static TokenType[] getLiterals() {
        return literals;
    }

    public static TokenType[] getTypes() {
        return types;
    }

    public static TokenType[] getUnaryOperators() {
        return unaryOperators;
    }

    public static <T> LinkedList<T> toLinkedList(List<T> list) {
        return new LinkedList<>(list);
    }

    public static boolean isLiteral(TokenType type) {
        switch (type) {
            case INT_LITERAL:
            case FLOAT_LITERAL:
            case BOOL_LITERAL:
            case STRING_LITERAL:
                return true;
            default:
                return false;
        }
    }

    public static boolean isType(TokenType type) {
        switch (type) {
            case INT:
            case FLOAT:
            case BOOL:
            case STRING:
                return true;
            default:
                return false;
        }
    }

    public static boolean isUnaryOperator(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBinaryOperator(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
            case ASTERISK:
            case F_SLASH:
                return true;
            default:
                return false;
        }
    }

    public static DataType toInternalType(TokenType type) throws Exception {
        if (isLiteral(type)) {
            switch (type) {
                case INT_LITERAL:
                    return DataType.INT;
                case FLOAT_LITERAL:
                    return DataType.FLOAT;
                case BOOL_LITERAL:
                    return DataType.BOOL;
                case STRING_LITERAL:
                    return DataType.STRING;
                default:
                    throw new Exception(MessageFormat.format("Unexpected literal {0}", PrintUtils.toPrintable(type)));
            }
        }

        throw new Exception(MessageFormat.format("Type {0} is not a literal", PrintUtils.toPrintable(type)));
    }

    public static BinaryOperatorType toBinaryOperator(TokenType type) throws Exception {
        if (isBinaryOperator(type)) {
            switch (type) {
                case PLUS:
                    return BinaryOperatorType.ADDITION;
                case MINUS:
                    return BinaryOperatorType.SUBTRACTION;
                case ASTERISK:
                    return BinaryOperatorType.MULTIPLICATION;
                case F_SLASH:
                    return BinaryOperatorType.DIVISION;
                default:
                    throw new Exception(MessageFormat.format("Unexpected binary operator {0}", PrintUtils.toPrintable(type)));
            }
        }

        throw new Exception(MessageFormat.format("Type {0} is not a binary operator", PrintUtils.toPrintable(type)));
    }

}
