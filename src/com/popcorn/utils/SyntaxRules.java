package com.popcorn.utils;

import com.popcorn.compiler.lexical.TokenType;

public class SyntaxRules {

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
