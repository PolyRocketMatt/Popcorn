package com.popcorn.compiler.node;

import java.util.LinkedList;

public abstract class Node {

    public enum NodeType {
        BinOpNode,
        UnaryOpNode,
        LiteralNode,
    }

    public abstract Node getSuperNode();

    public abstract void setSuperNode(Node node);

    public abstract LinkedList<Node> getSubNodes();

    public abstract void add(Node...nodes);

}
