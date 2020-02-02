package com.popcorn.utils;

import com.popcorn.utils.utilities.ConversionUtils;

public class ValueConstructor {

    public static InternalValue toPopcodeValue(Object value) {
        if (value instanceof Float) {
            try {
                Float f = Float.parseFloat(String.valueOf(value));

                return new InternalValue(f, ConversionUtils.DataType.FLOAT);
            } catch (NumberFormatException ex) { return null; }
        } else if (value instanceof Integer) {
            try {
                Integer i = Integer.parseInt(String.valueOf(value));

                return new InternalValue(i, ConversionUtils.DataType.INT);
            } catch (NumberFormatException ex) { return null; }
        } else if (value instanceof String) {
            if (value.equals("true")) {
                return new InternalValue(true, ConversionUtils.DataType.BOOL);
            } else if (value.equals("false")) {
                return new InternalValue(false, ConversionUtils.DataType.BOOL);
            } else {
                return new InternalValue(value, ConversionUtils.DataType.STRING);
            }
        } else {
            return null;
        }
    }

}
