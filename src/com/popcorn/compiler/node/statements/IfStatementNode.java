package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.StatementNode;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.utilities.ConversionUtils;

import java.util.ArrayList;

public class IfStatementNode implements StatementNode {

    private Node parentNode;
    private Token openParenthesisToken;
    private ExpressionNode expression;
    private Token closedParenthesisToken;
    private Token openBraceToken;
    private ArrayList<Node> body;

    public IfStatementNode(Token openParenthesisToken, ExpressionNode expression, Token closedParenthesisToken, Token openBraceToken) {
        this.parentNode = null;
        this.openParenthesisToken = openParenthesisToken;
        this.expression = expression;
        this.closedParenthesisToken = closedParenthesisToken;
        this.openBraceToken = openBraceToken;
        this.body = new ArrayList<>();
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void add(Node node) {
        body.add(node);
    }

    @Override
    public Node getParentNode() {
        return parentNode;
    }

    @Override
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public ArrayList<Node> getBody() {
        return body;
    }

    @Override
    public Node[] getChildren() {
        Node[] parent = new Node[] {
                openParenthesisToken,
                expression,
                closedParenthesisToken,
                openParenthesisToken
        };
        Node[] children = body.toArray(new Node[body.size()]);

        return ConversionUtils.concatenateArrays(parent, children);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.IF_STATEMENT_NODE;
    }
}
