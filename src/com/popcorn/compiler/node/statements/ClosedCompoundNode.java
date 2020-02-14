package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;

public class ClosedCompoundNode extends Node {

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.CLOSE_COMPOUND_NODE;
    }
}
