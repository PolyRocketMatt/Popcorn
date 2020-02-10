package com.popcorn.compiler.parser;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.TokenStream;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.node.ExpressionNode;
import com.popcorn.node.expressions.BinaryExpressionNode;
import com.popcorn.node.expressions.LiteralExpressionNode;
import com.popcorn.utils.SyntaxRules;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.LiteralValue;

public class PopcornParser {

    private DiagnosticsBag diagnostics;
    private TokenStream stream;

    public PopcornParser(DiagnosticsBag diagnostics, TokenStream stream) {
        this.diagnostics = diagnostics;
        this.stream = stream;
    }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public ExpressionNode parse() throws Exception {
        return parseExpression();
    }

    private ExpressionNode parseExpression() throws Exception {
        return parseBinaryExpression(0);
    }

    private ExpressionNode parseBinaryExpression(int parentPrecedence) throws Exception {
        ExpressionNode left = parsePrimitiveExpression();

        while (true) {
            int precedence = SyntaxRules.getBinaryOperatorPrecedence(current().getType());
            if (precedence == 0 || precedence <= parentPrecedence)
                break;

            Token operatorToken = get();
            ExpressionNode right = parseBinaryExpression(precedence);

            left = new BinaryExpressionNode(left, operatorToken, right);
        }

        return left;
    }

    private ExpressionNode parsePrimitiveExpression() throws Exception {
        try {
            Token literal = matchAny(ConversionUtils.getLiterals());
            ConversionUtils.DataType type = ConversionUtils.toInternalType(literal.getType());

            return new LiteralExpressionNode(new LiteralValue(type, literal.getValue()));
        } catch (Exception ex) {
            diagnostics.reportUnexpectedLiteralException(current().getType());
        }

        throw new Exception("Unexpected parser exception while parsing literal");
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
