package com.popcorn.compiler.parser;

import com.popcorn.compiler.exception.conversion.InternalValueException;
import com.popcorn.compiler.exception.conversion.LiteralToTypeException;
import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.TokenStream;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.expressions.LiteralExpressionNode;
import com.popcorn.utils.ConversionUtils;
import com.popcorn.utils.Diagnostics;
import com.popcorn.utils.InternalValue;

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

    public void parse() {
        if (stream.current().getType().equals(TokenType.SOF)) {
            stream.next();
            parseExpression();
        }
    }

    public ExpressionNode parseExpression() {
        return parsePrimaryExpression();
    }

    public LiteralExpressionNode parsePrimaryExpression() {
        // TODO: 02/02/2020 Parse parenthesized operation
        Token literal = stream.matchAny(ConversionUtils.literals);

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

}
