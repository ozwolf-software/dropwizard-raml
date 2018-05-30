package net.ozwolf.raml.generator.util;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;

public class ParameterUtils {
    public static String getTypeOf(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (Collection.class.isAssignableFrom(type))
            type = (Class<?>) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];

        if (type.isPrimitive())

        if (String.class.isAssignableFrom(type))
            return "string";

        if (Integer.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type) || type.getName().equals("integer") || type.getName().equals("long"))
            return "integer";

        if (Number.class.isAssignableFrom(type) || type.getName().equals("double") || type.getName().equals("float"))
            return "number";

        if (Boolean.class.isAssignableFrom(type) || type.getName().equals("boolean"))
            return "boolean";

        return "string";
    }

    public static Set<String> getAllowedValues(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (Collection.class.isAssignableFrom(type))
            type = (Class<?>) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];

        if (!Enum.class.isAssignableFrom(type))
            return newHashSet();

        Enum[] values = (Enum[]) type.getEnumConstants();

        return Arrays.stream(values).map(Enum::name).collect(toSet());
    }
}
