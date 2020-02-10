package com.popcorn.compiler.binding;

import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.compiler.binding.node.expressions.BoundBinaryExpressionNode;
import com.popcorn.compiler.binding.node.expressions.BoundLiteralExpressionNode;
import com.popcorn.compiler.binding.node.expressions.BoundUnaryExpressionNode;
import com.popcorn.compiler.binding.operators.BoundBinaryOperator;
import com.popcorn.compiler.binding.operators.BoundUnaryOperator;
import com.popcorn.node.ExpressionNode;
import com.popcorn.node.expressions.BinaryExpressionNode;
import com.popcorn.node.expressions.LiteralExpressionNode;
import com.popcorn.node.expressions.ParenthesizedExpression;
import com.popcorn.node.expressions.UnaryExpressionNode;
import com.popcorn.utils.diagnostics.DiagnosticsBag;

public class Binder {

    private DiagnosticsBag diagnostics;

    public Binder() { }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public BoundExpressionNode bindExpression(ExpressionNode node) throws Exception {
        switch (node.getNodeType()) {
            case LITERAL_NODE:
                return bindLiteralExpression((LiteralExpressionNode) node);
            case PARENTHESIZED_EXPRESSION_NODE:
                return bindParenthesizedExpression((ParenthesizedExpression) node);
            case UNARY_OPERATOR_NODE:
                return bindUnaryExpression((UnaryExpressionNode) node);
            case BINARY_OPERATOR_NODE:
                return bindBinaryExpression((BinaryExpressionNode) node);
            default:
                throw new Exception("Unexpected syntax " + node.getNodeType());
        }
    }

    private BoundExpressionNode bindLiteralExpression(LiteralExpressionNode node) {
        return new BoundLiteralExpressionNode(node.getValue());
    }

    private BoundExpressionNode bindParenthesizedExpression(ParenthesizedExpression node) throws Exception {
        return bindExpression(node.getExpression());
    }

    private BoundExpressionNode bindUnaryExpression(UnaryExpressionNode node) throws Exception {
        BoundExpressionNode boundOperand = bindExpression(node.getOperand());
        BoundUnaryOperator boundOperator = BoundUnaryOperator.bind(node.getOperatorToken().getType(), boundOperand.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedUnaryOperator(node.getOperatorToken().getValue(), boundOperand.getType());
            return boundOperand;
        }

        return new BoundUnaryExpressionNode(boundOperator, boundOperand);
    }

    private BoundExpressionNode bindBinaryExpression(BinaryExpressionNode node) throws Exception {
        BoundExpressionNode boundLeft = bindExpression(node.getLeft());
        BoundExpressionNode boundRight = bindExpression(node.getRight());
        BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(node.getOperatorToken().getType(), boundLeft.getType(), boundRight.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedBinaryOperator(node.getOperatorToken().getValue(), boundLeft.getType(), boundRight.getType());
            return boundLeft;
        }

        return new BoundBinaryExpressionNode(boundOperator, boundLeft, boundRight);
    }
}
