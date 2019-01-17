package net.ozwolf.raml.generator.model;

import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RamlParameterTypeTest {
    @Test
    public void shouldReturnStringType() {
        Parameter parameter = parameterOf("test");

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("string");
    }

    @Test
    public void shouldReturnStringTypeForList() {
        Parameter parameter = collectionParameterOf("test");

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("string");
    }

    @Test
    public void shouldReturnIntegerType() {
        Parameter parameter = parameterOf(1);

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("integer");
    }

    @Test
    public void shouldReturnIntegerTypeForList() {
        Parameter parameter = collectionParameterOf(1);

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("integer");
    }

    @Test
    public void shouldReturnNumberType() {
        Parameter parameter = parameterOf(1.0);

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("number");
    }

    @Test
    public void shouldReturnNumberTypeForList() {
        Parameter parameter = collectionParameterOf(1.0);

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("number");
    }

    @Test
    public void shouldReturnBooleanType() {
        Parameter parameter = parameterOf(true);

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("boolean");
    }

    @Test
    public void shouldReturnBooleanTypeForList() {
        Parameter parameter = collectionParameterOf(false);

        assertThat(RamlParameterType.getRamlType(parameter)).isEqualTo("boolean");
    }

    private static Parameter collectionParameterOf(Object o) {
        ParameterizedType type = mock(ParameterizedType.class);
        when(type.getActualTypeArguments()).thenReturn(new Type[]{o.getClass()});

        Parameter parameter = parameterOf(newArrayList(o));
        when(parameter.getParameterizedType()).thenReturn(type);

        return parameter;
    }

    private static Parameter parameterOf(Object o) {
        Class<?> c = o.getClass();
        Parameter parameter = mock(Parameter.class);
        Answer<Class<?>> a = i -> c;
        when(parameter.getType()).then(a);
        return parameter;
    }
}