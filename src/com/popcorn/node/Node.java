package com.popcorn.node;

import com.popcorn.utils.enums.NodeType;

public abstract class Node {

    public abstract Node[] getChildren();

    public abstract NodeType getNodeType();

}
