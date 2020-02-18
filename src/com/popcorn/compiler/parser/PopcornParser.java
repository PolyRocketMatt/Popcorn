package com.popcorn.compiler.parser;

import com.popcorn.compiler.lexical.Token;
import com.popcorn.compiler.lexical.TokenStream;
import com.popcorn.compiler.lexical.TokenType;
import com.popcorn.compiler.node.*;
import com.popcorn.compiler.node.expressions.*;
import com.popcorn.compiler.node.statements.*;
import com.popcorn.exception.PopcornException;
import com.popcorn.utils.enums.NodeType;
import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.rules.SyntaxRules;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.utilities.PrintUtils;
import com.popcorn.utils.values.LiteralValue;

public class PopcornParser {

    private DiagnosticsBag diagnostics;
    private TokenStream stream;

    private ParentNode parentNode;
    private Node superNode;

    public PopcornParser(DiagnosticsBag diagnostics, TokenStream stream) {
        this.diagnostics = diagnostics;
        this.stream = stream;

        parentNode = new ParentNode(null);
        superNode = parentNode;
    }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public TokenStream getStream() {
        return stream;
    }

    // TODO: 18/02/2020 FIX SUPER NODE STRUCTURE
    public SyntaxTree parse() throws PopcornException {
        Node objectStatementNode = parseObject();

        if (!(objectStatementNode instanceof SkipNode)) {
            parentNode.setObject(objectStatementNode);
        } else
            parentNode.setObject(new SkipNode());

        parentNode.setObject(objectStatementNode);

        match(TokenType.EOF);
        return new SyntaxTree(diagnostics.getDiagnostics(), parentNode);
    }

    private Node parseObject() throws PopcornException {
        Token match = match(TokenType.CLASS);
        Token identifier = match(TokenType.IDENTIFIER);
        match(TokenType.OPAREN);
        match(TokenType.CPAREN);
        match(TokenType.OBRACE);

        if (identifier.getValue() != null) {
            ObjectStatementNode node = new ObjectStatementNode(parentNode, identifier.getValue().toString());

            while (current().getType() != TokenType.CBRACE) {
                if (current().getType() == TokenType.EOF) {
                    break;
                }

                Node statement = parseStatement();

                node.getBody().add(statement);
            }

            match(TokenType.CBRACE);

            return node;
        } else
            diagnostics.reportExpectedObject(match.getType());

        return new SkipNode();
    }

    private Node parseStatement() throws PopcornException {
        switch (current().getType()) {
            case IF:
                next();
                Node ifStatementNode = parseIfStatement();
                match(TokenType.CBRACE);

                if (current().getType() != TokenType.ELSE_IF && current().getType() != TokenType.ELSE) {
                    superNode = ((IfStatementNode) ifStatementNode).getSuperNode();
                }

                return ifStatementNode;

            case ELSE_IF:
                next();
                Node elseIfStatementNode = parseElseIfStatement();
                match(TokenType.CBRACE);

                if (superNode.getNodeType() == NodeType.IF_STATEMENT_NODE) {
                    ((IfStatementNode) superNode).addClause((ElseIfStatementNode) elseIfStatementNode);
                } else {
                    diagnostics.reportInvalidClause(TokenType.ELSE_IF);
                }

                return new SkipNode();
            case ELSE:
                next();
                Node elseStatementNode = parseElseStatement();
                match(TokenType.CBRACE);

                if (superNode.getNodeType() == NodeType.IF_STATEMENT_NODE) {
                    if (((IfStatementNode) superNode).getElseStatementNode() == null) {
                        ((IfStatementNode) superNode).setElseStatementNode((ElseStatementNode) elseStatementNode);

                        superNode = superNode.getSuperNode();
                    } else {
                        diagnostics.reportAlreadyDefinedClause(TokenType.ELSE);
                    }
                } else {
                    diagnostics.reportInvalidClause(TokenType.ELSE);
                }
                return new SkipNode();

            case PRINT:
                next();

                return parsePrintStatement();
            default:
                return parseExpression();
        }
    }

    private Node parseIfStatement() throws PopcornException {
        Token openParenthesisToken = match(TokenType.OPAREN);
        ExpressionNode expression;

        if (current().getType() != TokenType.EOF)
            expression = parseBinaryExpression(0);
        else {
            diagnostics.reportMissingExpression(NodeType.IF_STATEMENT_NODE);
            expression = new LiteralExpressionNode(superNode, new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
        }

        Token closedParenthesisToken = match(TokenType.CPAREN);
        Token openBraceToken = match(TokenType.OBRACE);

        IfStatementNode node = new IfStatementNode(openParenthesisToken, expression, closedParenthesisToken, openBraceToken);

        Node nodeParent = superNode;
        superNode = node;

        while (current().getType() != TokenType.CBRACE && current().getType() != TokenType.EOF) {
            node.add(parseStatement());
        }

        node.setSuperNode(nodeParent);

        return node;
    }

    private Node parseElseIfStatement() throws PopcornException {
        Token openParenthesisToken = match(TokenType.OPAREN);
        ExpressionNode expression;

        if (current().getType() != TokenType.EOF)
            expression = parseBinaryExpression(0);
        else {
            diagnostics.reportMissingExpression(NodeType.ELSE_IF_STATEMENT_NODE);
            expression = new LiteralExpressionNode(superNode, new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
        }

        Token closedParenthesisToken = match(TokenType.CPAREN);
        Token openBraceToken = match(TokenType.OBRACE);

        ElseIfStatementNode node = new ElseIfStatementNode(openParenthesisToken, expression, closedParenthesisToken, openBraceToken);

        Node nodeParent = superNode;
        superNode = node;

        while (current().getType() != TokenType.CBRACE && current().getType() != TokenType.EOF) {
            node.add(parseStatement());
        }

        node.setSuperNode(nodeParent);
        superNode = nodeParent;

        return node;
    }

    private Node parseElseStatement() throws PopcornException {
        Token openBraceToken = match(TokenType.OBRACE);
        ElseStatementNode node = new ElseStatementNode(openBraceToken);

        Node nodeParent = superNode;
        superNode = node;

        while (current().getType() != TokenType.CBRACE && current().getType() != TokenType.EOF) {
            node.add(parseStatement());
        }

        node.setSuperNode(nodeParent);
        superNode = nodeParent;
        
        return node;
    }

    private Node parsePrintStatement() throws PopcornException {
        Token openParenthesisToken = match(TokenType.OPAREN);
        ExpressionNode expression;

        if (current().getType() != TokenType.EOF)
            expression = parseBinaryExpression(0);
        else {
            diagnostics.reportMissingExpression(NodeType.PRINT_STATEMENT_NODE);
            expression = new LiteralExpressionNode(superNode, new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
        }

        Token closedParenthesisToken = match(TokenType.CPAREN);
        PrintStatementNode node = new PrintStatementNode(openParenthesisToken, expression, closedParenthesisToken);

        // TODO: 15/02/2020 Maybe throw exception when parent is null
        // TODO: 15/02/2020 Parent Node should (in future) always be function
        if (superNode != null)
            node.setSuperNode(superNode);
        else
            node.setSuperNode(parentNode);
        //No need to update statement node, this is a one line statement!

        return node;
    }

    private ExpressionNode parseExpression() throws PopcornException {
        return parseAssignmentExpression();
    }

    // TODO: 14/02/2020 Add multi-variable assignments (a = b = c)
    private ExpressionNode parseAssignmentExpression() throws PopcornException {
        AssignmentExpressionNode node;
        
        if (ConversionUtils.isType(current().getType())) {
            ConversionUtils.DataType type = ConversionUtils.toInternalType(get().getType());
            Token identifierToken = match(TokenType.IDENTIFIER);
            Token equalsToken = match(TokenType.EQUAL);
            ExpressionNode value = parseAssignmentExpression();
            
            node = new AssignmentExpressionNode(superNode, type, identifierToken, equalsToken, value);
            value.setSuperNode(node);
            
            return node;
        } else if (current().getType() == TokenType.IDENTIFIER &&
                peek(1).getType() == TokenType.EQUAL) {
            Token identifierToken = match(TokenType.IDENTIFIER);
            Token equalsToken = match(TokenType.EQUAL);
            ExpressionNode value = parseAssignmentExpression();
            
            node = new AssignmentExpressionNode(superNode, identifierToken, equalsToken, value);
            value.setSuperNode(node);
            
            return node;
        }

        return parseBinaryExpression(0);
    }

    private ExpressionNode parseBinaryExpression(int parentPrecedence) throws PopcornException {
        ExpressionNode left;

        int unaryOperatorPrecedence = SyntaxRules.getUnaryOperatorPrecedence(current().getType());
        if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
            Token operatorToken = get();
            ExpressionNode operand = parseBinaryExpression(unaryOperatorPrecedence);

            left = new UnaryExpressionNode(superNode, operatorToken, operand);
        } else {
            left = parsePrimitiveExpression();
        }

        while (true) {
            int precedence = SyntaxRules.getBinaryOperatorPrecedence(current().getType());
            if (precedence == 0 || precedence <= parentPrecedence)
                break;

            Token operatorToken = get();
            ExpressionNode right = parseBinaryExpression(precedence);

            left = new BinaryExpressionNode(superNode, left, operatorToken, right);
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

                    return new ParenthesizedExpression(superNode, openParenthesisToken, expression, closedParenthesisToken);

                case IDENTIFIER:
                    Token identifierToken = get();

                    return new NameExpressionNode(superNode, identifierToken);

                default:
                    // TODO: 15/02/2020 Add NPE catch
                    Token literal = matchAny(ConversionUtils.getLiterals());
                    ConversionUtils.DataType type = ConversionUtils.toInternalType(literal.getType());

                    return new LiteralExpressionNode(superNode, new LiteralValue(type, ConversionUtils.toValueType(literal.getType()), literal.getValue()));
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
        return stream.match(type, true);
    }

    private Token matchAny(TokenType[] types) {
        return stream.matchAny(types);
    }
}
