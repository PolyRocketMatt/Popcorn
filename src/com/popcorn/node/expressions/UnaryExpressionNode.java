package com.popcorn.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.node.ExpressionNode;
import com.popcorn.node.Node;
import com.popcorn.utils.enums.UnaryOperatorType;
import com.popcorn.utils.utilities.ConversionUtils;

public class UnaryExpressionNode extends ExpressionNode {

    private Token operatorToken;
    private UnaryOperatorType operatorType;
    private ExpressionNode operand;

    public UnaryExpressionNode(Token operatorToken, ExpressionNode operand) {
        this.operatorToken = operatorToken;
        this.operand = operand;

        try {
            this.operatorType = ConversionUtils.toUnaryOperator(operatorToken.getType());
        } catch (Exception ex) {}
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
}
