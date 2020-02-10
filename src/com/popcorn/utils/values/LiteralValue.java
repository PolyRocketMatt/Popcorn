package com.popcorn.utils.values;

import com.popcorn.utils.utilities.ConversionUtils;

public class LiteralValue {

    private ConversionUtils.DataType type;
    private Object value;

    public LiteralValue(ConversionUtils.DataType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public ConversionUtils.DataType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
