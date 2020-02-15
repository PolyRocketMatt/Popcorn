package com.popcorn.compiler.lexical;

import com.popcorn.utils.diagnostics.DiagnosticsBag;
import com.popcorn.utils.utilities.ConversionUtils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    private DiagnosticsBag diagnostics;
    private String source;
    private TokenStream stream;
    private LinkedList<TokenData> tokenData;

    public Tokenizer() {
        this.diagnostics = new DiagnosticsBag();
        this.tokenData = new LinkedList<>();

        add(TokenType.FLOAT_LITERAL, "-?\\d+(\\.\\d+)?");
        add(TokenType.STRING_LITERAL,"\"(.*)?\"");
        add(TokenType.BOOL_LITERAL, "true|false");

        add(TokenType.PLUS, "[+]");
        add(TokenType.MINUS, "[-]");
        add(TokenType.ASTERISK, "[*]");
        add(TokenType.F_SLASH, "[/]");
        add(TokenType.MODULO, "[%]");
        add(TokenType.HAT, "\\^");
        add(TokenType.TILDE, "[~]");

        add(TokenType.OPAREN, "\\(");
        add(TokenType.CPAREN, "\\)");
        add(TokenType.OBRACE, "\\{");
        add(TokenType.CBRACE, "\\}");
        add(TokenType.OBRACKET, "\\[");
        add(TokenType.CBRACKET, "\\]");

        add(TokenType.DOUBLE_EQUALS, "\\=\\=");
        add(TokenType.NOT_EQUALS, "\\!\\=");
        add(TokenType.EQUAL, "\\=");
        add(TokenType.EXCLAMATION, "\\!");
        add(TokenType.GREATER_THAN, "\\>");
        add(TokenType.LESS_THAN, "\\<");
        add(TokenType.DOUBLE_AMPERSAND, "\\&\\&");
        add(TokenType.AMPERSAND, "\\&");
        add(TokenType.DOUBLE_PIPE, "\\|\\|");
        add(TokenType.PIPE, "\\|");
        add(TokenType.COLON, "\\:");
        add(TokenType.SEMI_COLON, "\\;");
        add(TokenType.DOT, "\\.");
        add(TokenType.COMMA, "\\,");
        add(TokenType.HASH, "\\#");

        add(TokenType.FLOAT, "float");
        add(TokenType.INT, "int");
        add(TokenType.STRING, "string");
        add(TokenType.BOOL, "bool");
        add(TokenType.CLASS, "class");
        add(TokenType.USE, "use");
        add(TokenType.NULL, "null");
        add(TokenType.RETURN, "return");
        add(TokenType.FOR, "for");
        add(TokenType.WHILE, "while");
        add(TokenType.IF, "if");
        add(TokenType.ELSE, "else");
        add(TokenType.ELSE_IF, "elseif");
        add(TokenType.TYPE_OF, "typeof");
        add(TokenType.VOID, "void");
        add(TokenType.PRINT, "print");
        add(TokenType.REPEAT, "repeat");

        add(TokenType.IDENTIFIER, "[a-zA-Z]([a-zA-Z0-9]*)?");
    }

    public static Tokenizer getTokenizer(String source) {
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setSource(source);

        return tokenizer;
    }

    private void add(TokenType type, String pattern) {
        tokenData.add(new TokenData(type, Pattern.compile("^(" + pattern +")")));
    }

    public void setSource(String source) {
        this.source = source;
    }

    public TokenStream getStream() {
        return stream;
    }

    public DiagnosticsBag getDiagnostics() {
        return diagnostics;
    }

    public void tokenize() {
        this.stream = new TokenStream();
        int lineIndex = 1;
        for (String line : source.split("\n")) {
            if (!line.startsWith("//")) {
                line = line.trim();
                lineIndex++;

                int length = line.length();
                while (!line.isEmpty()) {
                    int remaining = line.length();
                    boolean match = false;
                    for (TokenData data : tokenData) {
                        Matcher matcher = data.getPattern().matcher(line);

                        if (matcher.find()) {
                            String group = matcher.group().trim();
                            Object value = ConversionUtils.toValidRepresentation(data.getType(), group);
                            Token token;

                            if (data.getType() == TokenType.FLOAT_LITERAL) {
                                if (group.contains(".")) {
                                    token = new Token(TokenType.FLOAT_LITERAL, value, lineIndex, length - remaining);
                                } else {
                                    Integer intVal = Integer.parseInt(group);
                                    token = new Token(TokenType.INT_LITERAL, intVal, lineIndex, length - remaining);
                                }
                            } else {
                                token = new Token(data.getType(), value, lineIndex, length - remaining);
                            }

                            line = matcher.replaceFirst("").trim();
                            stream.add(token);
                            match = true;

                            break;
                        }
                    }

                    if (!match) {
                        diagnostics.reportInvalidInput("Bad character input {0}, not a valid token", lineIndex, length - remaining);

                        break;
                    }
                }
            }
        }

        stream.add(new Token(TokenType.EOF, "EOF", ++lineIndex, 0));
    }

}
