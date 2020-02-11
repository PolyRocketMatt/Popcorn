package com.popcorn.utils.utilities;

import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.utils.enums.BinaryOperatorType;
import com.popcorn.utils.enums.UnaryOperatorType;
import com.popcorn.utils.enums.ValueType;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class ConversionUtils {

    public enum DataType {
        INT,
        FLOAT,
        BOOL,
        STRING,

        NOT_DEFINED
    }

    private static final TokenType[] literals = { TokenType.INT_LITERAL, TokenType.FLOAT_LITERAL, TokenType.BOOL_LITERAL, TokenType.STRING_LITERAL, TokenType.NULL };
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

    public static int toIntegerRepresentation(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            System.out.println(value + " is not a valid int32");
        }

        return -1;
    }

    public static float toFloatRespresentation(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            System.out.println(value + " is not a valid int32");
        }

        return -1f;
    }

    public static boolean toBooleanRepresentation(String value) {
        return Boolean.parseBoolean(value);
    }

    public static Object toValidRepresentation(TokenType type, String value) {
        switch (type) {
            case INT_LITERAL:
                return toIntegerRepresentation(value);

            case FLOAT_LITERAL:
                return toFloatRespresentation(value);

            case BOOL_LITERAL:
                return toBooleanRepresentation(value);

            case STRING_LITERAL:
            default:
                return value;
        }
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

        if (isType(type)) {
            switch (type) {
                case INT:
                    return DataType.INT;

                case FLOAT:
                    return DataType.FLOAT;

                case BOOL:
                    return DataType.BOOL;

                case STRING:
                    return DataType.STRING;

                default:
                    throw new Exception(MessageFormat.format("Unexpected type {0}", PrintUtils.toPrintable(type)));
            }
        }

        throw new Exception(MessageFormat.format("Type {0} is not a literal", PrintUtils.toPrintable(type)));
    }

    public static UnaryOperatorType toUnaryOperator(TokenType type) throws Exception {
        if (isUnaryOperator(type)) {
            switch (type) {
                case PLUS:
                    return UnaryOperatorType.IDENTITY;

                case MINUS:
                    return UnaryOperatorType.NEGATION;

                case EXCLAMATION:
                    return UnaryOperatorType.LOGICAL_NEGATION;

                default:
                    throw new Exception(MessageFormat.format("Unexpected unary operator {0}", PrintUtils.toPrintable(type)));
            }
        }

        return UnaryOperatorType.NON_EXISTENT;
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

                case DOUBLE_AMPERSAND:
                    return BinaryOperatorType.LOGICAL_AND;

                case DOUBLE_PIPE:
                    return BinaryOperatorType.LOGICAL_OR;

                case DOUBLE_EQUALS:
                    return BinaryOperatorType.LOGICAL_EQUALS;

                case NOT_EQUALS:
                    return BinaryOperatorType.LOGICAL_NOT_EQUALS;

                default:
                    throw new Exception(MessageFormat.format("Unexpected binary operator {0}", PrintUtils.toPrintable(type)));
            }
        }

        return BinaryOperatorType.NON_EXISTENT;
    }

    public static ValueType toValueType(TokenType type) {
        if (isLiteral(type)) {
            switch (type) {
                case INT:
                    return ValueType.INT;

                case FLOAT:
                    return ValueType.FLOAT;

                case BOOL:
                    return ValueType.BOOL;

                case STRING:
                    return ValueType.STRING;

                case NULL:
                default:
                    return ValueType.NULL;
            }
        }

        return ValueType.NULL;
    }

}
