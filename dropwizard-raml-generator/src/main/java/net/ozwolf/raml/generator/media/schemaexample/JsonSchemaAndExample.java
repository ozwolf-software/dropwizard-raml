package net.ozwolf.raml.generator.media.schemaexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.generator.RamlGenerator;
import net.ozwolf.raml.generator.model.SchemaAndExample;
import net.ozwolf.raml.generator.util.JacksonUtils;
import net.ozwolf.raml.generator.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.function.Function;

public class JsonSchemaAndExample implements Function<RamlBody, SchemaAndExample> {
    @Override
    public SchemaAndExample apply(RamlBody annotation) {
        String schema = ResourceUtils.getResourceAsString(annotation.schema());
        String example = ResourceUtils.getResourceAsString(annotation.example());

        if (annotation.returnType() != RamlBody.NotDefinedReturnType.class) {
            return new SchemaAndExample(
                    JacksonUtils.toJsonSchema(annotation.returnType()),
                    Optional.ofNullable(fromMethod(annotation)).orElse(example)
            );
        } else {
            return new SchemaAndExample(schema, example);
        }
    }

    private String fromMethod(RamlBody annotation) {
        Class<?> type = annotation.returnType();

        RamlExample exampleAnnotation = type.getAnnotation(RamlExample.class);
        if (exampleAnnotation == null) return null;

        String methodName = exampleAnnotation.methodName();
        if (StringUtils.isBlank(methodName)) return null;

        try {
            Method method = type.getMethod(methodName);
            if (!Modifier.isStatic(method.getModifiers()))
                throw new IllegalArgumentException("Method [ " + methodName + " ] on [ " + type.getName() + " ] is not static.");

            Object example = method.invoke(type);

            return RamlGenerator.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(example);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to invoke method [ " + type.getName() + "#" + methodName + " ]", e);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Example of [ " + type.getName() + " ] could not be serialized to JSON.", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Zero argument static method [ " + methodName + " ] could not be found on [ " + type.getName() + " ] class.");
        }
    }
}
