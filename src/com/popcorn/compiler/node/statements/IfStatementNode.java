package com.popcorn.compiler.node.statements;

import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.StatementNode;
import com.popcorn.utils.enums.NodeType;

import java.util.ArrayList;

public class IfStatementNode extends StatementNode {

    private ExpressionNode expression;
    private ArrayList<Node> body;

    public IfStatementNode(ExpressionNode expression) {
        this.expression = expression;
        this.body = new ArrayList<>();
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void add(Node node) {
        body.add(node);
    }

    @Override
    public ArrayList<Node> getBody() {
        return body;
    }

    @Override
    public Node[] getChildren() {
        Node[] nodes = new Node[body.size() + 1];

        nodes[0] = expression;

        for (int i = 1; i < nodes.length; i++)
            nodes[i] = body.get(i);
        return nodes;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.IF_STATEMENT_NODE;
    }
}
