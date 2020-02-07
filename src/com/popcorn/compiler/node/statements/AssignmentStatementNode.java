package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;

import java.util.Arrays;
import java.util.LinkedList;

public class AssignmentStatementNode extends Node {

    private Node superNode;
    private LinkedList<Node> subNodes;

    // Node specific fields
    private String identifier;

    public AssignmentStatementNode(Node superNode, String identifier) {
        this.superNode = superNode;
        this.subNodes = new LinkedList<>();

        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Node getSuperNode() {
        return superNode;
    }

    @Override
    public void setSuperNode(Node node) {
        superNode = node;
    }

    @Override
    public LinkedList<Node> getSubNodes() {
        return subNodes;
    }

    @Override
    public void add(Node... nodes) {
        subNodes.addAll(Arrays.asList(nodes));
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.AssignmentNode;
    }
}
