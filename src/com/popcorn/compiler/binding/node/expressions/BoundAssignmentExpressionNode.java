package com.popcorn.compiler.binding.node.expressions;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.utils.enums.BoundNodeKind;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.VariableSymbol;

public class BoundAssignmentExpressionNode implements BoundExpressionNode {

    private VariableSymbol variable;
    private BoundExpressionNode expression;

    public BoundAssignmentExpressionNode(VariableSymbol variable, BoundExpressionNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public VariableSymbol getVariable() {
        return variable;
    }

    public BoundExpressionNode getExpression() {
        return expression;
    }

    @Override
    public ConversionUtils.DataType getType() {
        return variable.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ASSIGNMENT_EXPRESSION;
    }
}
