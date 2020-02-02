package com.popcorn.utils;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Diagnostics {

    private ArrayList<String> diagnostics;

    public Diagnostics() {
        this.diagnostics = new ArrayList<>();
    }

    public ArrayList<String> getDiagnostics() {
        return diagnostics;
    }

    public void add(String log, Object...operands) {
        diagnostics.add(MessageFormat.format(log, operands));
    }
}
