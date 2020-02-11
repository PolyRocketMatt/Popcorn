package com.popcorn.utils.utilities;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.expressions.LiteralExpressionNode;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.util.HashMap;
import java.util.Map;

public class PrintUtils {

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

    public static void prettyPrint(Node node, String indent, boolean isLast) {
        String marker = isLast ? "└──" : "├──";
        String nodeRep = indent + marker + node.getClass().getSimpleName();
        System.out.print(nodeRep);

        if (node instanceof Token) {
            System.out.print(": " + ((Token) node).getValue());
        } else if (node instanceof LiteralExpressionNode) {
            System.out.print(": " + ((LiteralExpressionNode) node).getValue().getValue());
        }

        System.out.println();

        indent += isLast ? "    " : "│   ";

        Node lastNode;

        if (node.getChildren().length != 0)
            lastNode = node.getChildren()[node.getChildren().length - 1];
        else
            lastNode = null;

        for (Node child : node.getChildren()) {
            if (lastNode != null)
                prettyPrint(child, indent, child == lastNode);
            else
                prettyPrint(child, indent, false);
        }
    }

    private static final int NAMES = 25;
    private static final int TYPES = 9;

    public static void prettyPrint(HashMap<VariableSymbol, LiteralValue> variables) {
        System.out.println("Variable Name            |   Type   |   Value");
        System.out.println("---------------------------------------------");

        for (Map.Entry entry : variables.entrySet()) {
            String name = ((VariableSymbol) entry.getKey()).getName();
            ConversionUtils.DataType type = ((VariableSymbol) entry.getKey()).getType();
            Object value = ((LiteralValue) entry.getValue()).getValue();

            int typeMargin = NAMES - name.length();
            int valueMargin = TYPES - type.toString().length();

            StringBuilder leftIndent = new StringBuilder("");
            StringBuilder middleIndent = new StringBuilder("");

            for (int i = 0; i < typeMargin; i++)
                leftIndent.append(" ");
            for (int i = 0; i < valueMargin; i++)
                middleIndent.append(" ");

            String finalString = name + leftIndent + "| " + type.toString() + middleIndent + "| " + value.toString();
            System.out.println(finalString);
        }

        System.out.println("---------------------------------------------");
    }
}
