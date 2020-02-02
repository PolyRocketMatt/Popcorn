package com.popcorn.utils;

import java.util.LinkedList;
import java.util.List;

public class ConversionUtils {

    public static <T> LinkedList<T> toLinkedList(List<T> list) {
        return new LinkedList<>(list);
    }

}
