package com.popcorn.node.expressions;

import com.popcorn.node.ExpressionNode;
import com.popcorn.node.Node;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.values.LiteralValue;

public class LiteralExpressionNode extends ExpressionNode {

    private LiteralValue value;

    public LiteralExpressionNode(LiteralValue value) {
        this.value = value;
    }

    public LiteralValue getValue() {
        return value;
    }

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.LITERAL_NODE;
    }
}
