package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.ParentNode;
import com.popcorn.compiler.node.StatementNode;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectStatementNode implements StatementNode {

    private Node superNode;
    private ArrayList<Node> body;
    private HashMap<VariableSymbol, LiteralValue> variables;
    private String name;

    public ObjectStatementNode(ParentNode superNode, String name) {
        this.superNode = superNode;
        this.body = new ArrayList<>();
        this.variables = new HashMap<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HashMap<VariableSymbol, LiteralValue> getVariables() {
        return variables;
    }

    @Override
    public Node getSuperNode() {
        return superNode;
    }

    @Override
    public ArrayList<Node> getBody() {
        return body;
    }

    @Override
    public Node[] getChildren() {
        return body.toArray(new Node[body.size()]);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.OBJECT_STATEMENT_NODE;
    }

    @Override
    public void setSuperNode(Node superNode) {
        this.superNode = superNode;
    }
}
