package com.popcorn.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.node.ExpressionNode;
import com.popcorn.node.Node;
import com.popcorn.utils.enums.BinaryOperatorType;
import com.popcorn.utils.utilities.ConversionUtils;

public class BinaryExpressionNode extends ExpressionNode {

    private Node left;
    private Token operatorToken;
    private BinaryOperatorType operatorType;
    private Node right;

    public BinaryExpressionNode(Node left, Token operatorToken, Node right) {
        this.left = left;
        this.operatorToken = operatorToken;
        this.right = right;

        try {
            this.operatorType = ConversionUtils.toBinaryOperator(operatorToken.getType());
        } catch (Exception ex) {}
    }

    public Node getLeft() {
        return left;
    }

    public BinaryOperatorType getOperatorType() {
        return operatorType;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public Node[] getChildren() {
        return new Node[] { left, operatorToken, right };
    }
}
