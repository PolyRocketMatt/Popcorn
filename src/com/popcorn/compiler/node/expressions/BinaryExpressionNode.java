package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.BinaryOperatorType;
import com.popcorn.utils.enums.NodeType;

import java.util.Arrays;
import java.util.LinkedList;

public class BinaryExpressionNode extends ExpressionNode {

    private Node superNode;
    private LinkedList<Node> subNodes;

    // Node specific fields
    private Token operatorToken;
    private BinaryOperatorType operation;
    private ExpressionNode left;
    private ExpressionNode right;

    public BinaryExpressionNode(Node superNode, Token operatorToken, BinaryOperatorType operation, ExpressionNode left, ExpressionNode right) {
        this.superNode = superNode;
        this.subNodes = new LinkedList<>();

        this.operatorToken = operatorToken;
        this.operation = operation;
        this.left = left;
        this.right = right;

        add(left);
        add(operatorToken);
        add(right);
    }

    public Token getOperatorToken() {
        return operatorToken;
    }

    public BinaryOperatorType getOperation() {
        return operation;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
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
        return NodeType.BinOpNode;
    }

}
