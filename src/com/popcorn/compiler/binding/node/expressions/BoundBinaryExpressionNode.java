package com.popcorn.compiler.binding.node.expressions;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.operators.BoundBinaryOperator;
import com.popcorn.utils.enums.BoundNodeKind;
import com.popcorn.utils.utilities.ConversionUtils;

public class BoundBinaryExpressionNode implements BoundExpressionNode {

    private BoundBinaryOperator operator;
    private BoundExpressionNode left;
    private BoundExpressionNode right;

    public BoundBinaryExpressionNode(BoundBinaryOperator operator, BoundExpressionNode left, BoundExpressionNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public BoundBinaryOperator getOperator() {
        return operator;
    }

    public BoundExpressionNode getLeft() {
        return left;
    }

    public BoundExpressionNode getRight() {
        return right;
    }

    @Override
    public ConversionUtils.DataType getType() {
        return operator.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BINARY_EXPRESSION;
    }
}
