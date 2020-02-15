package com.popcorn.compiler.binding.node;

import com.popcorn.utils.utilities.ConversionUtils;

public interface BoundExpressionNode extends BoundNode {

    ConversionUtils.DataType getType();

}
