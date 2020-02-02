package com.popcorn.utils;

import com.popcorn.compiler.exception.conversion.InternalValueException;
import com.popcorn.compiler.exception.conversion.LiteralToTypeException;
import com.popcorn.compiler.lexical.TokenType;

import java.util.LinkedList;
import java.util.List;

public class ConversionUtils {

    public enum DataType {
        INT,
        FLOAT,
        BOOL,
        STRING
    }

    public static TokenType[] literals = { TokenType.INT_LITERAL, TokenType.FLOAT_LITERAL, TokenType.BOOL_LITERAL, TokenType.STRING_LITERAL };
    public static TokenType[] types = { TokenType.VOID, TokenType.INT, TokenType.FLOAT_LITERAL, TokenType.BOOL_LITERAL, TokenType.STRING_LITERAL };

    public static <T> LinkedList<T> toLinkedList(List<T> list) {
        return new LinkedList<>(list);
    }

    public static String toPrintable(TokenType[] types) {
        StringBuilder builder = new StringBuilder("{");

        for (int i = 0; i < types.length; i++) {
            TokenType type = types[i];

            if (i != types.length - 1)
                builder.append(type.toString()).append(", ");
            else
                builder.append(type.toString());
        }

        builder.append("}");
        return builder.toString();
    }

    public static String toPrintable(TokenType type) {
        switch (type) {
            case INT_LITERAL:
            case FLOAT_LITERAL:
            case BOOL_LITERAL:
            case STRING_LITERAL:
                return "literal";

            case INT:
                return "int";
            case FLOAT:
                return "float";
            case BOOL:
                return "bool";
            case STRING:
                return "string";

            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case ASTERISK:
                return "*";
            case F_SLASH:
                return "/";
            case MODULO:
                return "%";
            case HAT:
                return "^";
            case TILDE:
                return "~";

            case OPAREN:
                return "(";
            case CPAREN:
                return ")";
            case OBRACE:
                return "{";
            case CBRACE:
                return "}";
            case OBRACKET:
                return "[";
            case CBRACKET:
                return "]";

            case EQUAL:
                return "=";
            case EXCLAMATION:
                return "!";
            case LESS_THAN:
                return "<";
            case GREATER_THAN:
                return ">";
            case AMPERSAND:
                return "&";
            case COLON:
                return ":";
            case SEMI_COLON:
                return ";";
            case DOT:
                return ".";
            case COMMA:
                return ",";
            case HASH:
                return "#";

            case CLASS:
                return "class";
            case USE:
                return "use";
            case NULL:
                return "null";
            case RETURN:
                return "return";
            case FOR:
                return "for";
            case WHILE:
                return "while";
            case IF:
                return "if";
            case ELSE:
                return "else";
            case ELSE_IF:
                return "elseif";
            case TYPE_OF:
                return "typeof";
            case VOID:
                return "void";
            case PRINT:
                return "print";
            case REPEAT:
                return "repeat";
            case SWAP:
                return "swap";

            case IDENTIFIER:
                return "identifier";
            case COMMENT:
                return "comment";

            case EOF:
                return "End Of File";
            case SOF:
                return "Start Of File";

            default:
                return "";
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

    public static DataType literalToDataType(TokenType type) throws LiteralToTypeException {
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

        throw new LiteralToTypeException("Inconvertible literal {0}", toPrintable(type));
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

}
