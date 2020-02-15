package com.popcorn.compiler.lexical;

import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;

public class Token implements Node {

    private final TokenType type;
    private final Object value;
    private final int line;
    private final int column;

    public Token(TokenType type, Object value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TOKEN_NODE;
    }

    @Override
    public String toString() {
        return "Token=[{Type=" + type.toString().toUpperCase() + "},{Value=" + value + "},{Line=" + line + ":Column=" + column + "}]";
    }
}
