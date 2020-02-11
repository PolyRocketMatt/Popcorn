package com.popcorn.compiler.binding.node.expressions;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.utils.enums.BoundNodeKind;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.VariableSymbol;

public class BoundVariableExpressionNode extends BoundExpressionNode {

    private VariableSymbol variable;

    public BoundVariableExpressionNode(VariableSymbol variable) {
        this.variable = variable;
    }

    public VariableSymbol getVariable() {
        return variable;
    }

    @Override
    public ConversionUtils.DataType getType() {
        return variable.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.VARIABLE_EXPRESSION;
    }
}