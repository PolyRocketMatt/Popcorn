package com.popcorn.compiler.node;

import java.util.ArrayList;

public interface StatementNode extends Node {

    StatementNode getParentNode();

    ArrayList<Node> getBody();

    void setParentNode(StatementNode node);

}
