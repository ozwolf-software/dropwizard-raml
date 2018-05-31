package net.ozwolf.raml.generator.util;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {
    public static <K, V> Map<K, V> nullIfEmpty(Map<K, V> value) {
        if (value == null || value.isEmpty()) return null;
        return value;
    }

    public static <V, C extends Collection<V>> C nullIfEmpty(C value) {
        if (value == null || value.isEmpty()) return null;
        return value;
    }
}
