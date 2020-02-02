package com.popcorn.utils.utilities;

import com.popcorn.compiler.exception.conversion.InternalValueException;
import com.popcorn.compiler.exception.conversion.InvalidOperatorException;
import com.popcorn.compiler.exception.conversion.InvalidTypeException;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.utils.enums.BinaryOperatorType;
import com.popcorn.utils.enums.UnaryOperatorType;

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

    public static DataType literalToDataType(TokenType type) throws InvalidTypeException {
        if (isLiteral(type)) {
            switch (type) {
                default:
                case INT_LITERAL:
                    return DataType.INT;
                case FLOAT_LITERAL:
                    return DataType.FLOAT;
                case BOOL_LITERAL:
                    return DataType.BOOL;
                case STRING_LITERAL:
                    return DataType.STRING;
            }
        }

        throw new InvalidTypeException("Inconvertible literal {0}", PrintUtils.toPrintable(type));
    }

    public static Object toInternalValue(DataType type, String value) throws InternalValueException {
        switch (type) {
            case INT:
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    throw new InternalValueException("{0} is not a valid integer", value);
                }
            case FLOAT:
                try {
                    return Float.parseFloat(value);
                } catch (NumberFormatException ex) {
                    throw new InternalValueException("{0 is not a valid float", value);
                }
            case BOOL:
                return Boolean.parseBoolean(value);
            case STRING:
                return value;
            default:
                throw new InternalValueException("{0} isn't a valid type");
        }
    }

    public static UnaryOperatorType toUnaryOpType(TokenType type) throws InvalidOperatorException {
        if (isUnaryOperator(type)) {
            switch (type) {
                default:
                case PLUS:
                    return UnaryOperatorType.IDENTITY;
                case MINUS:
                    return UnaryOperatorType.NEGATION;
            }
        }

        throw new InvalidOperatorException("Invalid unary operator {0}", PrintUtils.toPrintable(type));
    }

    public static BinaryOperatorType toBinaryOpType(TokenType type) throws InvalidOperatorException {
        if (isBinaryOperator(type)) {
            switch (type) {
                default:
                case PLUS:
                    return BinaryOperatorType.ADDITION;
                case MINUS:
                    return BinaryOperatorType.SUBTRACTION;
                case ASTERISK:
                    return BinaryOperatorType.MULTIPLICATION;
                case F_SLASH:
                    return BinaryOperatorType.DIVISION;
            }
        }

        throw new InvalidOperatorException("Invalid unary operator {0}", PrintUtils.toPrintable(type));
    }

    public static DataType toType(TokenType type) throws InvalidTypeException {
        if (isType(type)) {
            switch (type) {
                default:
                case INT:
                    return DataType.INT;
                case FLOAT:
                    return DataType.FLOAT;
                case BOOL:
                    return DataType.BOOL;
                case STRING:
                    return DataType.STRING;
            }
        }

        throw new InvalidTypeException("{0} is not a type", PrintUtils.toPrintable(type));
    }

}
