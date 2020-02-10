package com.popcorn.compiler.binding.node.expressions;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.utils.enums.BoundNodeKind;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.LiteralValue;

public class BoundLiteralExpressionNode extends BoundExpressionNode {

    private LiteralValue value;

    public BoundLiteralExpressionNode(LiteralValue value) {
        this.value = value;
    }

    @Override
    public ConversionUtils.DataType getType() {
        return value.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.LITERAL_EXPRESSION;
    }
}
