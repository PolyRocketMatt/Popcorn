package com.popcorn.compiler.parser;

import com.popcorn.compiler.exception.conversion.InternalValueException;
import com.popcorn.compiler.exception.conversion.InvalidOperatorException;
import com.popcorn.compiler.exception.conversion.LiteralToTypeException;
import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.TokenStream;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.expressions.BinaryExpressionNode;
import com.popcorn.compiler.node.expressions.LiteralExpressionNode;
import com.popcorn.compiler.node.expressions.UnaryExpressionNode;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.Diagnostics;
import com.popcorn.utils.InternalValue;
import com.popcorn.utils.SyntaxRules;

public class PopcornParser {

    private Diagnostics diagnostics;
    private TokenStream stream;

    private Node superNode;
    private Node bodyNode;
    private Node currentNode;

    public PopcornParser(Diagnostics diagnostics, TokenStream stream) {
        this.diagnostics = diagnostics;
        this.stream = stream;

        this.superNode = null;
        this.bodyNode = null;
        this.currentNode = null;
    }

    public Diagnostics getDiagnostics() {
        return diagnostics;
    }

    public Node getSuperNode() {
        return superNode;
    }

    public Node getBodyNode() {
        return bodyNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public Node parse() {
        if (stream.current().getType().equals(TokenType.SOF)) {
            stream.next();
            return parseExpression(0);
        }

        return null;
    }

    public ExpressionNode parseExpression(int parentPrecedence) {
        ExpressionNode left = null;

        int unaryOpPrecedence = SyntaxRules.getUnaryOperatorPrecedence(current().getType());
        if (unaryOpPrecedence != 0 && unaryOpPrecedence >= parentPrecedence) {
            Token operator = get();
            ExpressionNode operand = parseExpression(parentPrecedence);

            if (ConversionUtils.isUnaryOperator(operator.getType()))
                try {
                    left = new UnaryExpressionNode(
                            null,
                            operator,
                            ConversionUtils.toUnaryOpType(operator.getType()) ,
                            operand
                    );
                } catch (InvalidOperatorException ex) {
                    diagnostics.add(ex.getException());
                }
        } else {
            left = parsePrimaryExpression();
        }

        while (true) {
            // TODO: 02/02/2020 Expect a binary operator! Throw exception if not!
            int precedence = SyntaxRules.getBinaryOperatorPrecedence(current().getType());
            if (precedence == 0 || precedence <= parentPrecedence)
                break;

            Token operator = get();
            ExpressionNode right = parseExpression(precedence);

            if (ConversionUtils.isBinaryOperator(operator.getType())) {
                try {
                    left = new BinaryExpressionNode(
                            null,
                            operator,
                            ConversionUtils.toBinaryOpType(operator.getType()),
                            left,
                            right
                    );
                } catch (InvalidOperatorException ex) {
                    diagnostics.add(ex.getException());
                }
            }
        }

        return left;
    }

    public LiteralExpressionNode parsePrimaryExpression() {
        // TODO: 02/02/2020 Parse parenthesized operation
        Token literal = stream.matchAny(ConversionUtils.getLiterals());

        try {
            ConversionUtils.DataType type = ConversionUtils.literalToDataType(literal.getType());

            return new LiteralExpressionNode(
                    null,
                    new InternalValue(ConversionUtils.toInternalValue(type, literal.getValue()), type)
            );
        } catch (LiteralToTypeException ex) {
            diagnostics.add("Couldn't add literal {0} because {1} is not a valid type", literal.getValue(), literal.getType());
        } catch (InternalValueException ex) {
            diagnostics.add("\"{0}\" is not a valid representation of a literal", stream.current().getValue());
        }

        return new LiteralExpressionNode(null, new InternalValue(null, null));
    }

    // Wrapper Functions
    private Token current() {
        return stream.current();
    }

    private Token get() {
        return stream.get();
    }

    private Token getNext() {
        return stream.getNext();
    }

    private void next() {
        stream.next();
    }

    private Token peek(int offset) {
        return stream.peek(offset);
    }

    private int peekAny(TokenType type) {
        return stream.peekAny(type);
    }

    private Token skip(int offset) {
        return stream.skip(offset);
    }

    private Token match(TokenType type, boolean addDiagnostic) {
        return stream.match(type, addDiagnostic);
    }

    private Token matchAny(TokenType[] types) {
        return stream.matchAny(types);
    }

    private void rollback(int offset) {
        stream.rollback(offset);
    }

}
