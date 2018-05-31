package net.ozwolf.raml.generator.media.schemaexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.generator.RamlGenerator;
import net.ozwolf.raml.generator.media.SchemaAndExampleGenerator;
import net.ozwolf.raml.generator.model.SchemaAndExample;
import net.ozwolf.raml.generator.util.JacksonUtils;
import net.ozwolf.raml.generator.util.ClassPathUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

public class JsonSchemaAndExample implements SchemaAndExampleGenerator {
    @Override
    public SchemaAndExample generate(RamlBody annotation) {
        String schema = ClassPathUtils.getResourceAsString(annotation.schema());
        String example = ClassPathUtils.getResourceAsString(annotation.example());

        if (annotation.type() != RamlBody.NotDefinedReturnType.class) {
            return new SchemaAndExample(
                    JacksonUtils.toJsonSchema(annotation.type()),
                    Optional.ofNullable(fromMethod(annotation.type())).orElse(example)
            );
        } else {
            return new SchemaAndExample(schema, example);
        }
    }

    @Override
    public SchemaAndExample generate(Class<?> type) {
        return new SchemaAndExample(
                JacksonUtils.toJsonSchema(type),
                Optional.ofNullable(fromMethod(type)).orElse(null)
        );
    }

    private String fromMethod(Class<?> type) {
        Method method = Arrays.stream(type.getMethods())
                .filter(m -> m.isAnnotationPresent(RamlExample.class))
                .findFirst()
                .orElse(null);

        if (method == null)
            return null;

        if (!Modifier.isStatic(method.getModifiers()))
            throw new IllegalArgumentException("Method [ " + method.getName() + " ] on [ " + type.getName() + " ] is not static.");

        try {
            Object example = method.invoke(type);

            return RamlGenerator.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(example);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to invoke method [ " + type.getName() + "#" + method.getName() + " ]", e);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Example of [ " + type.getName() + " ] could not be serialized to JSON.", e);
        }
    }
}
