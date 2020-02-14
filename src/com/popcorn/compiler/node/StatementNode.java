package com.popcorn.compiler.node;

import java.util.ArrayList;

public abstract class StatementNode extends Node {

    public abstract ArrayList<Node> getBody();

}
