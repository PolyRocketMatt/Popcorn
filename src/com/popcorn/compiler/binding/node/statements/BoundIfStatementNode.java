package com.popcorn.compiler.binding.node.statements;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.BoundStatementNode;
import com.popcorn.utils.enums.BoundNodeKind;

import java.util.ArrayList;

public class BoundIfStatementNode implements BoundStatementNode {

    private BoundExpressionNode boundExpression;
    private ArrayList<BoundNode> boundNodes;

    public BoundIfStatementNode(BoundExpressionNode boundExpression, ArrayList<BoundNode> boundNodes) {
        this.boundExpression = boundExpression;
        this.boundNodes = boundNodes;
    }

    public BoundExpressionNode getBoundExpression() {
        return boundExpression;
    }

    public ArrayList<BoundNode> getBoundNodes() {
        return boundNodes;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.IF_STATEMENT;
    }
}
