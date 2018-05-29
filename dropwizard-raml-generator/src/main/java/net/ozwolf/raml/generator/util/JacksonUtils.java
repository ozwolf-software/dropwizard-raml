package net.ozwolf.raml.generator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import net.ozwolf.raml.generator.RamlGenerator;

public class JacksonUtils {
    public static String toJsonSchema(Class<?> type) {
        try {
            JsonSchemaGenerator generator = new JsonSchemaGenerator(RamlGenerator.MAPPER, JsonSchemaConfig.vanillaJsonSchemaDraft4());

            JsonNode schema = generator.generateJsonSchema(type);
            return RamlGenerator.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error generating schema for [ " + type.getName() + " ].", e);
        }
    }
}
