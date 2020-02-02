package com.popcorn.utils;

import com.popcorn.utils.utilities.ConversionUtils;

public class InternalValue {

    private Object value;
    private ConversionUtils.DataType type;

    public InternalValue(Object value, ConversionUtils.DataType type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public ConversionUtils.DataType getType() {
        return type;
    }
}
