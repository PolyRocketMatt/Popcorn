package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.StatementNode;
import com.popcorn.utils.enums.NodeType;

import java.util.ArrayList;

public class PrintStatementNode implements StatementNode {

    private Node parentNode;
    private Token openParenthesisToken;
    private ExpressionNode expression;
    private Token closedParenthesisToken;

    public PrintStatementNode(Token openParenthesisToken, ExpressionNode expression, Token closedParenthesisToken) {
        this.parentNode = null;
        this.openParenthesisToken = openParenthesisToken;
        this.expression = expression;
        this.closedParenthesisToken = closedParenthesisToken;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public Node getParentNode() {
        return parentNode;
    }

    @Override
    public ArrayList<Node> getBody() {
        return new ArrayList<Node>() {{
            add(expression);
        }};
    }

    @Override
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public Node[] getChildren() {
        return new Node[] { openParenthesisToken, expression, closedParenthesisToken};
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.PRINT_STATEMENT_NODE;
    }
}
