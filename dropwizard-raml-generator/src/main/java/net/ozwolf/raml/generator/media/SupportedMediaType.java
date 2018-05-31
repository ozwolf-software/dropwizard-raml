package net.ozwolf.raml.generator.media;

import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.generator.media.schemaexample.JsonSchemaAndExample;
import net.ozwolf.raml.generator.media.schemaexample.OtherSchemaAndExample;
import net.ozwolf.raml.generator.model.SchemaAndExample;
import net.ozwolf.raml.generator.util.ClassPathUtils;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;

public enum SupportedMediaType {
    JSON(MediaType.APPLICATION_JSON_TYPE, new JsonSchemaAndExample()),
    OTHER(MediaType.WILDCARD_TYPE, new OtherSchemaAndExample());

    private final MediaType media;
    private final SchemaAndExampleGenerator schemaAndExample;

    SupportedMediaType(MediaType media, SchemaAndExampleGenerator schemaAndExample) {
        this.media = media;
        this.schemaAndExample = schemaAndExample;
    }

    public static SchemaAndExample getSchemaAndExample(RamlBody annotation) {
        if (annotation.type() == RamlBody.NotDefinedReturnType.class)
            return new SchemaAndExample(
                    ClassPathUtils.getResourceAsString(annotation.schema()),
                    ClassPathUtils.getResourceAsString(annotation.example())
            );
        return find(annotation.contentType()).schemaAndExample.generate(annotation);
    }

    public static SchemaAndExample getSchemaAndExample(String contentType, Class<?> type) {
        return find(contentType).schemaAndExample.generate(type);
    }

    public static SupportedMediaType find(String contentType) {
        MediaType type = MediaType.valueOf(contentType);
        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(contentType) || v.media.isCompatible(type))
                .findFirst()
                .orElse(OTHER);
    }
}
