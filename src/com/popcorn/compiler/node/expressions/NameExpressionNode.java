package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;

public class NameExpressionNode extends ExpressionNode {

    private Token identifierToken;

    public NameExpressionNode(Token identifierToken) {
        this.identifierToken = identifierToken;
    }

    public Token getIdentifierToken() {
        return identifierToken;
    }

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NAME_NODE;
    }
}
