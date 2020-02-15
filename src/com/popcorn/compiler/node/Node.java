package com.popcorn.compiler.node;

import com.popcorn.utils.enums.NodeType;

public interface Node {

    Node[] getChildren();

    NodeType getNodeType();

}
