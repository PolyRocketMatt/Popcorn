package com.popcorn.compiler.parser;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.TokenStream;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.expressions.*;
import com.popcorn.utils.rules.SyntaxRules;
import com.popcorn.utils.SyntaxTree;
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

    public TokenStream getStream() {
        return stream;
    }

    public SyntaxTree parse() throws Exception {
        ExpressionNode expression = parseExpression();
        Token endOfFileToken = match(TokenType.EOF, true);

        diagnostics.getDiagnostics().addAll(stream.getDiagnostics().getDiagnostics());

        return new SyntaxTree(diagnostics.getDiagnostics(), expression, endOfFileToken);
    }

    private ExpressionNode parseExpression() throws Exception {
        return parseAssignmentExpression();
    }

    private ExpressionNode parseAssignmentExpression() throws Exception {
        if (ConversionUtils.isType(current().getType())) {
            ConversionUtils.DataType type = ConversionUtils.toInternalType(get().getType());
            Token identifierToken = match(TokenType.IDENTIFIER, true);
            Token equalsToken = match(TokenType.EQUAL, true);
            ExpressionNode value = parseAssignmentExpression();

            return new AssignmentExpressionNode(type, identifierToken, equalsToken, value);
        } else if (current().getType() == TokenType.IDENTIFIER &&
                peek(1).getType() == TokenType.EQUAL) {
            Token identifierToken = match(TokenType.IDENTIFIER, true);
            Token equalsToken = match(TokenType.EQUAL, true);
            ExpressionNode value = parseAssignmentExpression();

            return new AssignmentExpressionNode(identifierToken, equalsToken, value);
        }

        return parseBinaryExpression(0);
    }

    private ExpressionNode parseBinaryExpression(int parentPrecedence) throws Exception {
        ExpressionNode left;

        int unaryOperatorPrecedence = SyntaxRules.getUnaryOperatorPrecedence(current().getType());
        if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
            Token operatorToken = get();
            ExpressionNode operand = parseBinaryExpression(unaryOperatorPrecedence);

            left = new UnaryExpressionNode(operatorToken, operand);
        } else {
            left = parsePrimitiveExpression();
        }

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
            switch (current().getType()) {
                case OPAREN:
                    Token openParenthesisToken = get();
                    ExpressionNode expression = parseBinaryExpression(0);
                    Token closedParenthesisToken = match(TokenType.CPAREN, true);

                    return new ParenthesizedExpression(openParenthesisToken, expression, closedParenthesisToken);
                case IDENTIFIER:
                    Token identifierToken = get();

                    return new NameExpressionNode(identifierToken);
                default:
                    Token literal = matchAny(ConversionUtils.getLiterals());
                    ConversionUtils.DataType type = ConversionUtils.toInternalType(literal.getType());

                    return new LiteralExpressionNode(new LiteralValue(type, literal.getValue()));
            }
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
