package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.enums.UnaryOperatorType;
import com.popcorn.utils.utilities.ConversionUtils;

public class UnaryExpressionNode implements ExpressionNode {

    private Token operatorToken;
    private UnaryOperatorType operatorType;
    private ExpressionNode operand;

    public UnaryExpressionNode(Token operatorToken, ExpressionNode operand) {
        this.operatorToken = operatorToken;
        this.operand = operand;

        try {
            this.operatorType = ConversionUtils.toUnaryOperator(operatorToken.getType());
        } catch (Exception ex) {
            this.operatorType = UnaryOperatorType.NON_EXISTENT;
        }
    }

    public Token getOperatorToken() {
        return operatorToken;
    }

    public UnaryOperatorType getOperatorType() {
        return operatorType;
    }

    public ExpressionNode getOperand() {
        return operand;
    }

    @Override
    public Node[] getChildren() {
        return new Node[] { operatorToken, operand };
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.UNARY_OPERATOR_NODE;
    }
}
