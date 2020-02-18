package com.popcorn.utils.enums;

public enum VariableLevel {

    ILLEGAL(-1),
    OBJECT(0),
    METHOD(1),
    LOCAL(2);

    private int level;

    VariableLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
