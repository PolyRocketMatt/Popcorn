package com.popcorn.compiler.parser;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.TokenStream;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.ParentNode;
import com.popcorn.compiler.node.StatementNode;
import com.popcorn.compiler.node.expressions.*;
import com.popcorn.compiler.node.statements.IfStatementNode;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.rules.SyntaxRules;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.NullValue;


public class PopcornParser {

    private DiagnosticsBag diagnostics;
    private TokenStream stream;

    private ParentNode parentNode;
    private StatementNode statementNode;

    public PopcornParser(DiagnosticsBag diagnostics, TokenStream stream) {
        this.diagnostics = diagnostics;
        this.stream = stream;

        parentNode = new ParentNode();
        statementNode = null;
    }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public TokenStream getStream() {
        return stream;
    }

    public ParentNode getParentNode() {
        return parentNode;
    }

    public SyntaxTree parse() throws PopcornException {
        parentNode.getNodes().add(parseIfStatement());

        while (current().getType() != TokenType.EOF) {
            parentNode.getNodes().add(parseIfStatement());
        }

        match(TokenType.EOF);

        diagnostics.getDiagnostics().addAll(stream.getDiagnostics().getDiagnostics());

        return new SyntaxTree(diagnostics.getDiagnostics(), parentNode);
    }

    private Node parseIfStatement() throws PopcornException {
        if (current().getType() == TokenType.IF) {
            next();
            match(TokenType.OPAREN);
            ExpressionNode expression = parseBinaryExpression(0);
            match(TokenType.CPAREN);
            match(TokenType.OBRACE);

            IfStatementNode node = new IfStatementNode(expression);

            while (current().getType() != TokenType.CBRACE || current().getType() != TokenType.EOF) {
                node.add(parseExpression());
            }

            if (statementNode != null)
                node.setParentNode(statementNode);
            statementNode = node;

            match(TokenType.CBRACE);

            return node;
        }

        return parseExpression();
    }

    private ExpressionNode parseExpression() throws PopcornException {
        return parseAssignmentExpression();
    }

    // TODO: 14/02/2020 Add multi-variable assignments (a = b = c)
    private ExpressionNode parseAssignmentExpression() throws PopcornException {
        if (ConversionUtils.isType(current().getType())) {
            ConversionUtils.DataType type = ConversionUtils.toInternalType(get().getType());
            Token identifierToken = match(TokenType.IDENTIFIER);
            Token equalsToken = match(TokenType.EQUAL);
            ExpressionNode value = parseAssignmentExpression();

            return new AssignmentExpressionNode(type, identifierToken, equalsToken, value);
        } else if (current().getType() == TokenType.IDENTIFIER &&
                peek(1).getType() == TokenType.EQUAL) {
            Token identifierToken = match(TokenType.IDENTIFIER);
            Token equalsToken = match(TokenType.EQUAL);
            ExpressionNode value = parseAssignmentExpression();

            return new AssignmentExpressionNode(identifierToken, equalsToken, value);
        }

        return parseBinaryExpression(0);
    }

    private ExpressionNode parseBinaryExpression(int parentPrecedence) throws PopcornException {
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

    private ExpressionNode parsePrimitiveExpression() throws PopcornException {
        try {
            switch (current().getType()) {
                case OPAREN:
                    Token openParenthesisToken = get();
                    ExpressionNode expression = parseBinaryExpression(0);
                    Token closedParenthesisToken = match(TokenType.CPAREN);

                    return new ParenthesizedExpression(openParenthesisToken, expression, closedParenthesisToken);

                case NULL:
                    Token nullToken = get();

                    return new NullExpressionNode(nullToken, new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, new NullValue()));

                case IDENTIFIER:
                    Token identifierToken = get();

                    return new NameExpressionNode(identifierToken);

                default:
                    // TODO: 15/02/2020 Add NPE catch
                    Token literal = matchAny(ConversionUtils.getLiterals());
                    ConversionUtils.DataType type = ConversionUtils.toInternalType(literal.getType());

                    return new LiteralExpressionNode(new LiteralValue(type, ConversionUtils.toValueType(literal.getType()), literal.getValue()));
            }
        } catch (Exception ex) {
            diagnostics.reportUnexpectedLiteralException(current().getType());
        }

        throw new PopcornException("Couldn't not parse type {0} as a literal", current().getType());
    }

    // Wrapper Functions
    private Token current() {
        return stream.current();
    }

    private Token get() {
        return stream.get();
    }

    private void next() {
        stream.next();
    }

    private Token peek(int offset) {
        return stream.peek(offset);
    }

    private Token match(TokenType type) {
        try {
            return stream.match(type);
        } catch (PopcornException ex) {
            return null;
        }
    }

    private Token matchAny(TokenType[] types) {
        try {
            return stream.matchAny(types);
        } catch (PopcornException ex) {
            return null;
        }
    }
}
