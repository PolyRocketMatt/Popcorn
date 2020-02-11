package com.popcorn.utils.values;

import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.utilities.ConversionUtils;

public class LiteralValue {

    private ConversionUtils.DataType type;
    private ValueType valueType;
    private Object value;

    public LiteralValue(ConversionUtils.DataType type, ValueType valueType, Object value) {
        this.type = type;
        this.valueType = valueType;
        this.value = value;
    }

    public ConversionUtils.DataType getType() {
        return type;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public Object getValue() {
        return value;
    }
}
