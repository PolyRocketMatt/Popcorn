package com.popcorn.utils.utilities;

import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.enums.BinaryOperatorType;
import com.popcorn.utils.enums.UnaryOperatorType;
import com.popcorn.utils.enums.ValueType;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ConversionUtils {

    public enum DataType {
        INT,
        FLOAT,
        BOOL,
        STRING,
        DYNAMIC,

        NOT_DEFINED
    }

    private static final TokenType[] literals = { TokenType.INT_LITERAL, TokenType.FLOAT_LITERAL, TokenType.BOOL_LITERAL, TokenType.STRING_LITERAL, TokenType.NULL };
    private static final TokenType[] types = { TokenType.VOID, TokenType.INT, TokenType.FLOAT, TokenType.BOOL, TokenType.STRING };
    private static final TokenType[] unaryOperators = { TokenType.PLUS, TokenType.MINUS, TokenType.EXCLAMATION };
    private static final TokenType[] binaryOperators = { TokenType.PLUS, TokenType.MINUS, TokenType.ASTERISK, TokenType.F_SLASH, TokenType.MODULO,
                                                            TokenType.DOUBLE_AMPERSAND, TokenType.DOUBLE_PIPE, TokenType.DOUBLE_EQUALS, TokenType.NOT_EQUALS };

    public static TokenType[] getLiterals() {
        return literals;
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

    public static float toFloatRepresentation(String value) {
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
                return toFloatRepresentation(value);

            case BOOL_LITERAL:
                return toBooleanRepresentation(value);

            case STRING_LITERAL:
            default:
                return value;
        }
    }

    public static boolean isLiteral(TokenType type) {
        return Arrays.stream(literals).anyMatch(tokenType -> tokenType == type);
    }

    public static boolean isType(TokenType type) {
        return Arrays.stream(types).anyMatch(tokenType -> tokenType == type);
    }

    public static boolean isUnaryOperator(TokenType type) {
        return Arrays.stream(unaryOperators).anyMatch(tokenType -> tokenType == type);
    }

    public static boolean isBinaryOperator(TokenType type) {
        return Arrays.stream(binaryOperators).anyMatch(tokenType -> tokenType == type);
    }

    public static DataType toInternalType(TokenType type) throws PopcornException {
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
                    throw new PopcornException("Unexpected literal {0}", PrintUtils.toPrintable(type));
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
                    throw new PopcornException("Unexpected type {0}", PrintUtils.toPrintable(type));
            }
        }

        throw new PopcornException("Type {0} is not a literal", PrintUtils.toPrintable(type));
    }

    public static UnaryOperatorType toUnaryOperator(TokenType type) throws PopcornException {
        if (isUnaryOperator(type)) {
            switch (type) {
                case PLUS:
                    return UnaryOperatorType.IDENTITY;

                case MINUS:
                    return UnaryOperatorType.NEGATION;

                case EXCLAMATION:
                    return UnaryOperatorType.LOGICAL_NEGATION;

                default:
                    throw new PopcornException("Unexpected unary operator {0}", PrintUtils.toPrintable(type));
            }
        }

        throw new PopcornException("Unexpected unary operator {0}", PrintUtils.toPrintable(type));
    }

    public static BinaryOperatorType toBinaryOperator(TokenType type) throws PopcornException {
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
                    throw new PopcornException("Unexpected binary operator {0}", PrintUtils.toPrintable(type));
            }
        }

        throw new PopcornException("Unexpected binary operator {0}", PrintUtils.toPrintable(type));
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

    @SafeVarargs
    public static <T> T[] concatenateArrays(T[]... arrays) {
        int finalLength = 0;
        for (T[] array : arrays) {
            finalLength += array.length;
        }

        T[] dest = null;

        int destPos = 0;
        for (T[] array : arrays) {
            if (dest == null) {
                dest = Arrays.copyOf(array, finalLength);
                destPos = array.length;
            } else {
                System.arraycopy(array, 0, dest, destPos, array.length);
                destPos += array.length;
            }
        }

        return dest;
    }

}
