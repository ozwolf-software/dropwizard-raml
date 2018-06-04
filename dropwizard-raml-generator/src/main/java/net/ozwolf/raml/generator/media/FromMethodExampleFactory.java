package net.ozwolf.raml.generator.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ozwolf.raml.annotations.RamlExample;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

public class FromMethodExampleFactory implements MediaFactory {
    @Override
    public Optional<String> create(Class<?> type, ObjectMapper mapper) {
        Method method = Arrays.stream(type.getMethods())
                .filter(m -> m.isAnnotationPresent(RamlExample.class))
                .findFirst()
                .orElse(null);

        if (method == null)
            return Optional.empty();

        if (!Modifier.isStatic(method.getModifiers()))
            throw new IllegalArgumentException("Method [ " + method.getName() + " ] on [ " + type.getName() + " ] is not static.");

        try {
            Object example = method.invoke(type);

            return Optional.ofNullable(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(example));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to invoke method [ " + type.getName() + "#" + method.getName() + " ]", e);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Example of [ " + type.getName() + " ] could not be serialized to JSON.", e);
        }
    }
}
