package com.popcorn.compiler.exception;

import java.text.MessageFormat;

public abstract class PopcornException extends Exception {

    private String exception;

    public PopcornException(String exception, Object...args) {
        this.exception = MessageFormat.format(exception, args);
    }

    public String getException() {
        return exception;
    }

}
