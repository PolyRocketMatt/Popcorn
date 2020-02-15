package com.popcorn.utils.rules;

import com.popcorn.compiler.lexical.TokenType;

public class SyntaxRules {

    private SyntaxRules() {}

    public static int getBinaryOperatorPrecedence(TokenType type) {
        switch (type) {
            case ASTERISK:
            case F_SLASH:
            case MODULO:
                return 5;

            case PLUS:
            case MINUS:
                return 4;

            case DOUBLE_EQUALS:
            case NOT_EQUALS:
                return 3;

            case DOUBLE_AMPERSAND:
                return 2;

            case DOUBLE_PIPE:
                return 1;

            default:
                return 0;
        }
    }

    public static int getUnaryOperatorPrecedence(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
            case EXCLAMATION:
                return 6;

            default:
                return 0;
        }
    }

}
