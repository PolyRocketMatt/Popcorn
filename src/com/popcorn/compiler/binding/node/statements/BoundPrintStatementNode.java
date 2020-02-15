package com.popcorn.compiler.binding.node.statements;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.node.BoundStatementNode;
import com.popcorn.utils.enums.BoundNodeKind;

public class BoundPrintStatementNode implements BoundStatementNode {

    private BoundExpressionNode boundExpression;

    public BoundPrintStatementNode(BoundExpressionNode boundExpression) {
        this.boundExpression = boundExpression;
    }

    public BoundExpressionNode getBoundExpression() {
        return boundExpression;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.PRINT_STATEMENT;
    }
}
