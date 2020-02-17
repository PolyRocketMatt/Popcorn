package com.popcorn.compiler.binding.node.statements;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.BoundStatementNode;
import com.popcorn.utils.enums.BoundNodeKind;

import java.util.ArrayList;

public class BoundIfStatementNode implements BoundStatementNode {

    private BoundExpressionNode boundExpression;
    private ArrayList<BoundNode> boundNodes;
    private ArrayList<BoundElseIfStatementNode> elseIfStatementNodes;
    private BoundElseStatementNode boundElseStatement;

    public BoundIfStatementNode(BoundExpressionNode boundExpression, ArrayList<BoundNode> boundNodes, ArrayList<BoundElseIfStatementNode> elseIfStatementNodes, BoundElseStatementNode boundElseStatement) {
        this.boundExpression = boundExpression;
        this.boundNodes = boundNodes;
        this.elseIfStatementNodes = elseIfStatementNodes;
        this.boundElseStatement = boundElseStatement;
    }

    public BoundExpressionNode getBoundExpression() {
        return boundExpression;
    }

    public ArrayList<BoundNode> getBoundNodes() {
        return boundNodes;
    }

    public ArrayList<BoundElseIfStatementNode> getElseIfStatementNodes() {
        return elseIfStatementNodes;
    }

    public BoundElseStatementNode getBoundElseStatement() {
        return boundElseStatement;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.IF_STATEMENT;
    }
}
