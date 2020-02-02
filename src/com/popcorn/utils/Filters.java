package com.popcorn.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Filters {

    private Filters() {}

    public static LinkedHashSet<String> filterDuplicates(ArrayList<String> strings) {
        return new LinkedHashSet<>(strings);
    }

}
