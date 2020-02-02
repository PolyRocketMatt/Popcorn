package com.popcorn.compiler.lexical;

import com.popcorn.compiler.node.Node;

import java.util.LinkedList;

public class Token extends Node {

    private final TokenType type;
    private final String value;
    private final int line;
    private final int column;

    public Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public Node getSuperNode() {
        return null;
    }

    @Override
    public void setSuperNode(Node node) {

    }

    @Override
    public LinkedList<Node> getSubNodes() {
        return new LinkedList<>();
    }

    @Override
    public void add(Node... nodes) {

    }

    @Override
    public String toString() {
        return "Token=[{Type=" + type.toString().toUpperCase() + "},{Value=" + value + "},{Line=" + line + ":Column=" + column + "}]";
    }
}
