package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.UnaryOperatorType;

import java.util.LinkedList;

public class UnaryExpressionNode extends ExpressionNode {

    private Node superNode;

    // Node specific fields
    private UnaryOperatorType operation;
    private ExpressionNode operand;

    public UnaryExpressionNode(Node superNode, UnaryOperatorType operation, ExpressionNode operand) {
        this.superNode = superNode;

        this.operation = operation;
        this.operand = operand;
    }

    public UnaryOperatorType getOperation() {
        return operation;
    }

    public ExpressionNode getOperand() {
        return operand;
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
    public void add(Node... nodes) {

    }
}
