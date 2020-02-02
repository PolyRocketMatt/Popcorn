package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.enums.UnaryOperatorType;

import java.util.Arrays;
import java.util.LinkedList;

public class UnaryExpressionNode extends ExpressionNode {

    private Node superNode;
    private LinkedList<Node> subNodes;

    // Node specific fields
    private Token operatorToken;
    private UnaryOperatorType operation;
    private ExpressionNode operand;

    public UnaryExpressionNode(Node superNode, Token operatorToken, UnaryOperatorType operation, ExpressionNode operand) {
        this.superNode = superNode;
        this.subNodes = new LinkedList<>();

        this.operatorToken = operatorToken;
        this.operation = operation;
        this.operand = operand;

        add(operatorToken);
        add(operand);
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
        return subNodes;
    }

    @Override
    public void add(Node... nodes) {
        subNodes.addAll(Arrays.asList(nodes));
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.UnaryOpNode;
    }

}
