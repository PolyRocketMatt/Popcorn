package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;

import java.util.Arrays;
import java.util.LinkedList;

public class ParenthesizedExpressionNode extends ExpressionNode {

    private Node superNode;
    private LinkedList<Node> subNodes;

    // Node specific fields
    private Token left;
    private Token right;
    private ExpressionNode expression;

    public ParenthesizedExpressionNode(Node superNode, Token left, Token right, ExpressionNode expression) {
        this.superNode = superNode;
        this.subNodes = new LinkedList<>();

        this.left = left;
        this.right = right;
        this.expression = expression;

        add(expression);
    }

    public Token getLeft() {
        return left;
    }

    public Token getRight() {
        return right;
    }

    public ExpressionNode getExpression() {
        return expression;
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
}
