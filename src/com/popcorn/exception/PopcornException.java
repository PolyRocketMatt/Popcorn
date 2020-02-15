package com.popcorn.exception;

import java.text.MessageFormat;

public class PopcornException extends Exception {

    public PopcornException(String message, Object...objects) {
        super(MessageFormat.format(message, objects));
    }

}
