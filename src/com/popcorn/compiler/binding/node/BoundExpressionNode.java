package com.popcorn.compiler.binding.node;

import com.popcorn.utils.utilities.ConversionUtils;

public abstract class BoundExpressionNode implements BoundNode {

    public abstract ConversionUtils.DataType getType();

}
