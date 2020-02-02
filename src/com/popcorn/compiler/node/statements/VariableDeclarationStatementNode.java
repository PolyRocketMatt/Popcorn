package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.utilities.ConversionUtils;

import java.util.Arrays;
import java.util.LinkedList;

public class VariableDeclarationStatementNode extends Node {

    private Node superNode;
    private LinkedList<Node> subNodes;

    // Node specific fields
    private ConversionUtils.DataType type;
    private String name;

    public VariableDeclarationStatementNode(Node superNode, ConversionUtils.DataType type, String name) {
        this.superNode = superNode;
        this.subNodes = new LinkedList<>();

        this.type = type;
        this.name = name;
    }

    public ConversionUtils.DataType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public Node getSuperNode() {
        return superNode;
    }

    @Override
    public void setSuperNode(Node superNode) {
        this.superNode = superNode;
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
        return NodeType.VariableNode;
    }
}
