package com.popcorn.utils.values;

import com.popcorn.utils.enums.VariableLevel;
import com.popcorn.utils.utilities.ConversionUtils;

public class VariableSymbol {

    private String name;
    private ConversionUtils.DataType type;
    private VariableScope scope;

    public VariableSymbol(String name, ConversionUtils.DataType type, VariableScope scope) {
        this.name = name;
        this.type = type;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public ConversionUtils.DataType getType() {
        return type;
    }

    public VariableScope getScope() {
        return scope;
    }
}
