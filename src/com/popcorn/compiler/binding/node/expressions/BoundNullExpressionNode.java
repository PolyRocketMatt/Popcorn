package com.popcorn.compiler.binding.node.expressions;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.utils.enums.BoundNodeKind;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.LiteralValue;

public class BoundNullExpressionNode extends BoundExpressionNode {

    private LiteralValue nullValue;

    public BoundNullExpressionNode(LiteralValue nullValue) {
        this.nullValue = nullValue;
    }

    public LiteralValue getNullValue() {
        return nullValue;
    }

    @Override
    public ConversionUtils.DataType getType() {
        return ConversionUtils.DataType.NOT_DEFINED;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.NULL_EXPRESSION;
    }
}
