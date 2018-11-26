package net.ozwolf.raml.generator.media.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import net.ozwolf.raml.generator.media.MediaFactory;

import java.util.Optional;

public class JsonSchemaFactory implements MediaFactory {
    @Override
    public Optional<String> create(Class<?> type, boolean collection, ObjectMapper mapper) {
        return Optional.of(toJsonSchema(type, collection, mapper));
    }

    private static String toJsonSchema(Class<?> type, boolean collection, ObjectMapper mapper) {
        try {
            JsonSchemaConfig config = JsonSchemaConfig.nullableJsonSchemaDraft4();
            JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper, config);

            JsonNode schema = collection ? generator.generateJsonSchema(collectionof(type)) : generator.generateJsonSchema(type);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error generating schema for [ " + type.getName() + " ].", e);
        }
    }

    private static JavaType collectionof(Class<?> type) {
        return TypeFactory.defaultInstance().constructArrayType(type);
    }
}
