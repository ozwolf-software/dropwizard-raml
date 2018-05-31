package net.ozwolf.raml.generator.model;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Predicate;

import static net.ozwolf.raml.generator.util.ParameterUtils.getRealType;

public enum RamlParameterType {
    STRING("string", String.class::isAssignableFrom),
    INTEGER("integer", t -> Integer.class.isAssignableFrom(t) || Long.class.isAssignableFrom(t) || t.getName().equals("integer") || t.getName().equals("long")),
    NUMBER("number", t -> Number.class.isAssignableFrom(t) || t.getName().equals("double") || t.getName().equals("float")),
    BOOLEAN("boolean", t -> Boolean.class.isAssignableFrom(t) || t.getName().equals("boolean"));

    private final String typeName;
    private final Predicate<Class<?>> test;

    RamlParameterType(String typeName, Predicate<Class<?>> test) {
        this.typeName = typeName;
        this.test = test;
    }

    public static String getRamlType(Parameter parameter) {
        Class<?> type = getRealType(parameter);

        return Arrays.stream(values())
                .filter(t -> t.test.test(type))
                .findFirst()
                .map(t -> t.typeName)
                .orElse(STRING.typeName);
    }
}
