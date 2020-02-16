package com.popcorn.compiler.binding.node.statements;

import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.BoundStatementNode;
import com.popcorn.utils.enums.BoundNodeKind;

import java.util.ArrayList;

public class BoundElseStatementNode implements BoundStatementNode {

    private ArrayList<BoundNode> boundNodes;

    public BoundElseStatementNode(ArrayList<BoundNode> boundNodes) {
        this.boundNodes = boundNodes;
    }

    public ArrayList<BoundNode> getBoundNodes() {
        return boundNodes;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ELSE_STATEMENT;
    }

}
