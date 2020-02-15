package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.BinaryOperatorType;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.utilities.ConversionUtils;

public class BinaryExpressionNode implements ExpressionNode {

    private ExpressionNode left;
    private Token operatorToken;
    private BinaryOperatorType operatorType;
    private ExpressionNode right;

    public BinaryExpressionNode(ExpressionNode left, Token operatorToken, ExpressionNode right) {
        this.left = left;
        this.operatorToken = operatorToken;
        this.right = right;

        try {
            this.operatorType = ConversionUtils.toBinaryOperator(operatorToken.getType());
        } catch (Exception ignored) {
            this.operatorType = BinaryOperatorType.NON_EXISTENT;
        }
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public Token getOperatorToken() {
        return operatorToken;
    }

    public BinaryOperatorType getOperatorType() {
        return operatorType;
    }

    public ExpressionNode getRight() {
        return right;
    }

    @Override
    public Node[] getChildren() {
        return new Node[] { left, operatorToken, right };
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.BINARY_OPERATOR_NODE;
    }
}
