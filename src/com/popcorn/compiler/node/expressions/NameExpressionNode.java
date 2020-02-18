package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;

public class NameExpressionNode implements ExpressionNode {

    private Node superNode;
    private Token identifierToken;

    public NameExpressionNode(Node superNode, Token identifierToken) {
        this.superNode = superNode;
        this.identifierToken = identifierToken;
    }

    public Token getIdentifierToken() {
        return identifierToken;
    }

    @Override
    public Node getSuperNode() {
        return superNode;
    }

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NAME_NODE;
    }

    @Override
    public void setSuperNode(Node superNode) {
        this.superNode = superNode;
    }
}
