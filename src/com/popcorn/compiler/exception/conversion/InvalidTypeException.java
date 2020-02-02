package com.popcorn.compiler.exception.conversion;

import com.popcorn.compiler.exception.PopcornException;

public class InvalidTypeException extends PopcornException {

    public InvalidTypeException(String exception, Object...args) {
        super(exception, args);
    }

}
