package net.ozwolf.raml.generator.media.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.kjetland.jackson.jsonSchema.SubclassesResolver;
import com.kjetland.jackson.jsonSchema.SubclassesResolverImpl;
import net.ozwolf.raml.generator.media.MediaFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonSchemaFactory implements MediaFactory {
    private final ObjectMapper mapper;
    private final JsonSchemaGenerator generator;

    public JsonSchemaFactory(ObjectMapper mapper){
        this(null, mapper);
    }

    public JsonSchemaFactory(String baseResolverPackage, ObjectMapper mapper) {
        this.mapper = mapper;
        this.generator = generator(baseResolverPackage, mapper);
    }

    @Override
    public Optional<String> create(Class<?> type, boolean collection) {
        return Optional.of(toJsonSchema(type, collection));
    }

    private String toJsonSchema(Class<?> type, boolean collection) {
        try {
            JsonNode schema = collection ? generator.generateJsonSchema(collectionOf(type)) : generator.generateJsonSchema(type);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error generating schema for [ " + type.getName() + " ].", e);
        }
    }

    private static JavaType collectionOf(Class<?> type) {
        return TypeFactory.defaultInstance().constructArrayType(type);
    }

    private static JsonSchemaGenerator generator(String basePackage, ObjectMapper mapper) {
        return new JsonSchemaGenerator(mapper, config(basePackage));
    }

    private static JsonSchemaConfig config(String basePackage) {
        JsonSchemaConfig config = JsonSchemaConfig.nullableJsonSchemaDraft4();
        if (StringUtils.isNotBlank(basePackage))
            config = config.withSubclassesResolver(resolver(basePackage));

        return config;
    }

    private static SubclassesResolver resolver(String basePackage) {
        List<String> packages = new ArrayList<>();
        packages.add(basePackage);
        return new SubclassesResolverImpl().withPackagesToScan(packages);
    }
}
