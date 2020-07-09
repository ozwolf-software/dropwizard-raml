package net.ozwolf.raml.generator.util;

import net.ozwolf.raml.annotations.RamlParameter;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;

public class ParameterUtils {
    public static Class<?> getRealType(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (Collection.class.isAssignableFrom(type))
            return getCollectionSubType(parameter);
        return type;
    }

    @SuppressWarnings("rawtypes")
    public static Set<String> getAllowedValues(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (Collection.class.isAssignableFrom(type))
            type = getCollectionSubType(parameter);

        if (!Enum.class.isAssignableFrom(type))
            return newHashSet();

        Enum[] values = (Enum[]) type.getEnumConstants();

        return Arrays.stream(values).map(Enum::name).collect(toSet());
    }

    public static Set<String> getAllowedValues(RamlParameter annotation) {
        if (annotation.allowedValuesEnum() != RamlParameter.NoEnum.class) {
            return Arrays.stream(annotation.allowedValuesEnum().getEnumConstants())
                    .map(Enum::name)
                    .collect(toSet());
        } else {
            return newHashSet(annotation.allowedValues());
        }
    }

    private static Class<?> getCollectionSubType(Parameter parameter) {
        Type type = ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
        if (type instanceof WildcardType)
            return (Class<?>) ((WildcardType) type).getUpperBounds()[0];

        return (Class<?>) type;
    }
}
