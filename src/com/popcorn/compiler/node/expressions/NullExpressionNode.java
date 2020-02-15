package com.popcorn.compiler.node.expressions;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.values.LiteralValue;

public class NullExpressionNode implements ExpressionNode {

    private Token nullToken;
    private LiteralValue nullValue;

    public NullExpressionNode(Token nullToken, LiteralValue nullValue) {
        this.nullToken = nullToken;
        this.nullValue = nullValue;
    }

    public LiteralValue getNullValue() {
        return nullValue;
    }

    @Override
    public Node[] getChildren() {
        return new Node[0];
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NULL_NODE;
    }
}
