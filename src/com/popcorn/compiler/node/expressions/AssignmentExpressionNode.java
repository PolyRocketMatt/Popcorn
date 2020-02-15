package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.utilities.ConversionUtils;

public class AssignmentExpressionNode implements ExpressionNode {

    private ConversionUtils.DataType type;
    private Token identifierToken;
    private Token equalsToken;
    private ExpressionNode expression;

    public AssignmentExpressionNode(ConversionUtils.DataType type, Token identifierToken, Token equalsToken, ExpressionNode expression) {
        this.type = type;
        this.identifierToken = identifierToken;
        this.equalsToken = equalsToken;
        this.expression = expression;
    }

    public AssignmentExpressionNode(Token identifierToken, Token equalsToken, ExpressionNode expression) {
        this.type = ConversionUtils.DataType.NOT_DEFINED;
        this.identifierToken = identifierToken;
        this.equalsToken = equalsToken;
        this.expression = expression;
    }

    public ConversionUtils.DataType getType() {
        return type;
    }

    public Token getIdentifierToken() {
        return identifierToken;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public Node[] getChildren() {
        return new Node[] { identifierToken, equalsToken, expression };
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ASSIGNMENT_NODE;
    }
}
