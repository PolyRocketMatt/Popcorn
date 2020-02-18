package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.values.LiteralValue;

public class LiteralExpressionNode implements ExpressionNode {

    private Node superNode;
    private LiteralValue value;

    public LiteralExpressionNode(Node superNode, LiteralValue value) {
        this.superNode = superNode;
        this.value = value;
    }

    public LiteralValue getValue() {
        return value;
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
        return NodeType.LITERAL_NODE;
    }

    @Override
    public void setSuperNode(Node superNode) {
        this.superNode = superNode;
    }
}
