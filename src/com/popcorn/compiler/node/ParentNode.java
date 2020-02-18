package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

import java.util.ArrayList;

public class ParentNode implements StatementNode {

    private Node object;

    public ParentNode(Node object) { this.object = object; }

    public Node getObject() {
        return object;
    }

    public void setObject(Node object) {
        this.object = object;
    }

    @Override
    public Node getSuperNode() {
        return null;
    }

    // TODO: 16/02/2020 Add all statements to this body
    @Override
    public ArrayList<Node> getBody() {
        return new ArrayList<Node>() {{ add(object); }};
    }

    @Override
    public Node[] getChildren() {
        return new Node[] { object };
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.PARENT_NODE;
    }

    @Override
    public void setSuperNode(Node node) {}
}
