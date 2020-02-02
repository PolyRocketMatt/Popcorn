package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.InternalValue;

import java.util.LinkedList;

public class LiteralExpressionNode extends ExpressionNode {

    private Node superNode;

    // Node specific fields
    private InternalValue value;

    public LiteralExpressionNode(Node superNode, InternalValue value) {
        this.superNode = superNode;

        this.value = value;
    }

    public InternalValue getValue() {
        return value;
    }

    @Override
    public Node getSuperNode() {
        return superNode;
    }

    @Override
    public void setSuperNode(Node superNode) {
        this.superNode = superNode;
    }

    @Override
    public LinkedList<Node> getSubNodes() {
        return new LinkedList<>();
    }

    @Override
    public void add(Node... nodes) { }
}
