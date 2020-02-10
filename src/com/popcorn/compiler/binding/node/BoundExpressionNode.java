package com.popcorn.compiler.binding.node;

import com.popcorn.utils.utilities.ConversionUtils;

public abstract class BoundExpressionNode extends BoundNode {

    public abstract ConversionUtils.DataType getType();

}
