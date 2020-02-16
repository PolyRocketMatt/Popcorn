package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

public class SkipNode implements Node {

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.SKIP_NODE;
    }
}
