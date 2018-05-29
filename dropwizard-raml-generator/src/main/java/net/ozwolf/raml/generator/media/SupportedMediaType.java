package net.ozwolf.raml.generator.media;

import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.generator.media.schemaexample.JsonSchemaAndExample;
import net.ozwolf.raml.generator.model.SchemaAndExample;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.function.Function;

public enum SupportedMediaType {
    JSON(MediaType.APPLICATION_JSON_TYPE, new JsonSchemaAndExample()),
    OTHER(MediaType.WILDCARD_TYPE, v -> SchemaAndExample.NONE);

    private final MediaType media;
    private final Function<RamlBody, SchemaAndExample> schemaAndExample;

    SupportedMediaType(MediaType media, Function<RamlBody, SchemaAndExample> schemaAndExample) {
        this.media = media;
        this.schemaAndExample = schemaAndExample;
    }

    public static SchemaAndExample getSchemaAndExample(RamlBody annotation) {
        return find(annotation.contentType()).schemaAndExample.apply(annotation);
    }

    public static SupportedMediaType find(String contentType) {
        MediaType type = MediaType.valueOf(contentType);
        return Arrays.stream(values())
                .filter(v -> v.media.isCompatible(type))
                .findFirst()
                .orElse(OTHER);
    }
}
