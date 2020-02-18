package com.popcorn.compiler.node;

import java.util.ArrayList;

public interface StatementNode extends Node {

    ArrayList<Node> getBody();

}
