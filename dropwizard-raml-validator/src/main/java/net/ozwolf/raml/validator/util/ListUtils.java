package net.ozwolf.raml.validator.util;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ListUtils {
    public static <T> List<T> getOrEmpty(List<T> value) {
        return value == null ? newArrayList() : value;
    }
}
