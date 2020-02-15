package com.popcorn.compiler.node;

import java.util.ArrayList;

public interface StatementNode extends Node {

    Node getParentNode();

    ArrayList<Node> getBody();

    void setParentNode(Node node);

}
