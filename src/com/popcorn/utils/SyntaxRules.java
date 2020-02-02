package com.popcorn.utils;

import com.popcorn.compiler.lexical.TokenType;

public class SyntaxRules {

    public static int getBinaryOperatorPrecedence(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
                return 1;
            case ASTERISK:
            case F_SLASH:
                return 2;
            default:
                return 0;
        }
    }

    public static int getUnaryOperatorPrecedence(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
                return 3;
            default:
                return 0;
        }
    }

}
