package net.ozwolf.raml.generator.media.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import net.ozwolf.raml.generator.media.MediaFactory;

import java.util.Optional;

public class JsonSchemaFactory implements MediaFactory {
    @Override
    public Optional<String> create(Class<?> type, ObjectMapper mapper) {
        return Optional.of(toJsonSchema(type, mapper));
    }

    private static String toJsonSchema(Class<?> type, ObjectMapper mapper) {
        try {
            JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper, JsonSchemaConfig.vanillaJsonSchemaDraft4());

            JsonNode schema = generator.generateJsonSchema(type);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error generating schema for [ " + type.getName() + " ].", e);
        }
    }
}
