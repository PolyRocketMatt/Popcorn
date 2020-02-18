package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

import java.util.ArrayList;

public class SkipNode implements Node {

    @Override
    public Node getSuperNode() {
        return null;
    }

    public ArrayList<Node> getBody() { return new ArrayList<>(); }

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.SKIP_NODE;
    }

    @Override
    public void setSuperNode(Node node) {

    }
}
