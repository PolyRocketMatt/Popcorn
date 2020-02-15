package com.popcorn.compiler.lexical;

import com.popcorn.exception.PopcornException;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.utilities.PrintUtils;

import java.util.LinkedList;

public class TokenStream {

    private DiagnosticsBag diagnostics;
    private LinkedList<Token> tokens;
    private int index;

    public TokenStream() {
        this.diagnostics = new DiagnosticsBag();
        this.tokens = new LinkedList<>();
        this.index = 0;
    }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public void add(Token token) {
        tokens.add(token);
    }

    public Token current() {
        return tokens.get(index);
    }

    public Token get() {
        if (index == tokens.size() - 1)
            return tokens.get(index);
        else {
            Token token = tokens.get(index);
            index++;

            return token;
        }
    }

    public Token getNext() {
        next();

        return get();
    }

    public void next() {
        if (index != tokens.size() - 1)
            index++;
    }

    public Token peek(int offset) {
        if (index + offset < tokens.size() - 1)
            return tokens.get(index + offset);
        else
            return tokens.get(tokens.size() - 1);
    }

    public int peekAny(TokenType type) {
        if (index == tokens.size() - 1) {
            return index;
        } else {
            LinkedList<Token> subTokens = ConversionUtils.toLinkedList(tokens.subList(index, tokens.size() - 1));

            for (int i = 0; i < subTokens.size(); i++) {
                Token token = subTokens.get(i);

                if (token.getType() == type) {
                    return i;
                }
            }

            return -1;
        }
    }

    public Token skip(int offset) {
        if (index + offset < tokens.size() - 1) {
            Token token = tokens.get(index + offset);
            index += (offset + 1);

            return token;
        } else {
            index = tokens.size() - 1;

            return tokens.get(tokens.size() - 1);
        }
    }

    public Token match(TokenType type, boolean diagnose) {
        if (current().getType().equals(type))
            return get();

        if (diagnose)
            diagnostics.reportUnexpectedToken(current().getType(), new TokenType[] { type });
        return new Token(type, null, current().getLine(), current().getColumn());
    }

    public Token matchAny(TokenType[] types) {
        for (TokenType type : types) {
            Token optional = match(type, false);

            if (optional.getValue() != null) {
                return optional;
            }
        }

        diagnostics.reportUnexpectedToken(current().getType(), types);
        return new Token(types[0], null, current().getLine(), current().getColumn());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[STREAM:     {\n");
        for (Token t : tokens) {
            builder.append(t.toString()).append("\n");
        }
        builder.append("}]");

        return builder.toString();
    }

}
