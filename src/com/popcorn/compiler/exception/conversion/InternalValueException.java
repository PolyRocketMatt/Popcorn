package com.popcorn.compiler.exception.conversion;

import com.popcorn.compiler.exception.PopcornException;

public class InternalValueException extends PopcornException {

    public InternalValueException(String exception, Object...args) {
        super(exception, args);
    }

}
