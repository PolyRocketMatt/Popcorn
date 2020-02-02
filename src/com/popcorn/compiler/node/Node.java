package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

import java.util.LinkedList;

public abstract class Node {

    public abstract Node getSuperNode();

    public abstract void setSuperNode(Node node);

    public abstract LinkedList<Node> getSubNodes();

    public abstract void add(Node...nodes);

    public abstract NodeType getNodeType();

}
