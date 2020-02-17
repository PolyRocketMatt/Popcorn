package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.ParentNode;
import com.popcorn.compiler.node.StatementNode;
import com.popcorn.utils.enums.NodeType;

import java.util.ArrayList;

public class ObjectStatementNode implements StatementNode {

    private ParentNode parentNode;
    private ArrayList<Node> body;
    private String name;

    public ObjectStatementNode(ParentNode parentNode, String name) {
        this.parentNode = parentNode;
        this.body = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public StatementNode getParentNode() {
        return parentNode;
    }

    @Override
    public ArrayList<Node> getBody() {
        return body;
    }

    @Override
    public void setParentNode(StatementNode node) {}

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.OBJECT_STATEMENT_NODE;
    }
}
