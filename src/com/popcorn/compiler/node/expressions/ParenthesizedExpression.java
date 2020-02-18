package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;

public class ParenthesizedExpression implements ExpressionNode {

    private Node superNode;
    private Token openParenthesisToken;
    private ExpressionNode expression;
    private Token closedParenthesisToken;

    public ParenthesizedExpression(Node superNode, Token openParenthesisToken, ExpressionNode expression, Token closedParenthesisToken) {
        this.superNode = superNode;
        this.openParenthesisToken = openParenthesisToken;
        this.expression = expression;
        this.closedParenthesisToken = closedParenthesisToken;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public Node getSuperNode() {
        return superNode;
    }

    @Override
    public Node[] getChildren() {
        return new Node[] { openParenthesisToken, expression, closedParenthesisToken };
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.PARENTHESIZED_EXPRESSION_NODE;
    }

    @Override
    public void setSuperNode(Node superNode) {
        this.superNode = superNode;
    }
}
