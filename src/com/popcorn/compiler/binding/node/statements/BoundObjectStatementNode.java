package com.popcorn.compiler.binding.node.statements;

import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.binding.node.BoundStatementNode;
import com.popcorn.utils.enums.BoundNodeKind;

import java.util.ArrayList;

public class BoundObjectStatementNode implements BoundStatementNode {

    private String name;
    private ArrayList<BoundNode> boundNodes;

    public BoundObjectStatementNode(String name, ArrayList<BoundNode> boundNodes) {
        this.name = name;
        this.boundNodes = boundNodes;
    }

    public String getName() {
        return name;
    }

    public ArrayList<BoundNode> getBoundNodes() {
        return boundNodes;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.OBJECT_STATEMENT;
    }
}
