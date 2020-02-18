package com.popcorn.utils.values;

import com.popcorn.utils.enums.VariableLevel;

public class VariableScope {

    private VariableLevel outsideLevel;
    private int insideLevel;

    public VariableScope(VariableLevel outsideLevel, int insideLevel) {
        this.outsideLevel = outsideLevel;
        this.insideLevel = insideLevel;
    }

    public VariableLevel getOutsideLevel() {
        return outsideLevel;
    }

    public int getInsideLevel() {
        return insideLevel;
    }

}
