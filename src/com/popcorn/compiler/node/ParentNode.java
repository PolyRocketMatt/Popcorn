package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

import java.util.ArrayList;
import java.util.List;

public class ParentNode implements StatementNode {

    private List<Node> nodes;

    public ParentNode() {
        nodes = new ArrayList<>();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public StatementNode getParentNode() {
        return null;
    }

    // TODO: 16/02/2020 Add all statements to this body
    @Override
    public ArrayList<Node> getBody() {
        return new ArrayList<>();
    }

    @Override
    public void setParentNode(StatementNode node) { }

    @Override
    public Node[] getChildren() {
        return nodes.toArray(new Node[nodes.size()]);
    }

    @Override
    public NodeType getNodeType() {
        return null;
    }
}
