package com.popcorn.compiler.exception.conversion;

import com.popcorn.compiler.exception.PopcornException;

public class InvalidOperatorException extends PopcornException {

    public InvalidOperatorException(String exception, Object...args) {
        super(exception, args);
    }

}
