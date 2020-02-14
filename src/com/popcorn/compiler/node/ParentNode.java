package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

import java.util.ArrayList;
import java.util.List;

public class ParentNode extends Node {

    private List<Node> nodes;

    public ParentNode() {
        nodes = new ArrayList<>();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public Node[] getChildren() {
        return nodes.toArray(new Node[nodes.size()]);
    }

    @Override
    public NodeType getNodeType() {
        return null;
    }
}
