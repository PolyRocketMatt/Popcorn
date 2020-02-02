package com.popcorn.compiler.lexical;

import java.util.regex.Pattern;

public class TokenData {

    private final TokenType type;
    private final Pattern pattern;

    public TokenData(TokenType type, Pattern pattern) {
        this.type = type;
        this.pattern = pattern;
    }

    public TokenType getType() {
        return type;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
