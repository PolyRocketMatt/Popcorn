package com.popcorn.utils.values;

import com.popcorn.utils.utilities.ConversionUtils;

public class VariableSymbol {

    private String name;
    private ConversionUtils.DataType type;

    public VariableSymbol(String name, ConversionUtils.DataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ConversionUtils.DataType getType() {
        return type;
    }
}
