package com.popcorn.compiler.binding.node.expressions;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.operators.BoundUnaryOperator;
import com.popcorn.utils.enums.BoundNodeKind;
import com.popcorn.utils.utilities.ConversionUtils;

public class BoundUnaryExpressionNode implements BoundExpressionNode {

    private BoundUnaryOperator operator;
    private BoundExpressionNode operand;

    public BoundUnaryExpressionNode(BoundUnaryOperator operator, BoundExpressionNode operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public BoundUnaryOperator getOperator() {
        return operator;
    }

    public BoundExpressionNode getOperand() {
        return operand;
    }

    @Override
    public ConversionUtils.DataType getType() {
        return operator.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UNARY_EXPRESSION;
    }
}
