package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

public interface Node {

    Node getSuperNode();

    Node[] getChildren();

    NodeType getNodeType();

    void setSuperNode(Node node);

}
