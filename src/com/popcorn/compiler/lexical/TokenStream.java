package com.popcorn.compiler.lexical;

import com.popcorn.utils.ConversionUtils;

import java.util.LinkedList;
import java.util.List;

public class TokenStream {

    private LinkedList<Token> tokens;
    private int index;

    public TokenStream() {
        this.tokens = new LinkedList<>();
        this.index = 0;
    }

    public TokenStream(List<Token> tokens) {
        this.tokens = ConversionUtils.toLinkedList(tokens);
        this.index = 0;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public int getIndex() {
        return index;
    }

    public static TokenStream fromStream(TokenStream stream) {
        return new TokenStream(stream.getTokens());
    }

    public void add(Token token) {
        tokens.add(token);
    }

    public Token current() {
        return tokens.get(index);
    }

    public Token get() {
        if (index == tokens.size() - 1) {
            return tokens.get(index);
        } else {
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
        if (index != tokens.size() - 1) {
            index++;
        }
    }

    public Token peek(int offset) {
        if (index + offset < tokens.size() - 1) {
            return tokens.get(index + offset);
        } else {
            return tokens.get(tokens.size() - 1);
        }
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

    public void rollback(int offset) {
        if (index - offset >= 0) {
            index = index - offset;
        } else {
            index = 0;
        }
    }

    public boolean expect(TokenType type) {
        return current().getType().equals(type);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[STREAM:     {");
        for (Token t : tokens) {
            builder.append(t.toString()).append("\n");
        }
        builder.append("}]");

        return builder.toString();
    }

}
