package com.popcorn.compiler.exception.conversion;

import com.popcorn.compiler.exception.PopcornException;

public class LiteralToTypeException extends PopcornException {

    public LiteralToTypeException(String exception, Object...args) {
        super(exception, args);
    }

}
