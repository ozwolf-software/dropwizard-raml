package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.generator.RamlMedia;
import net.ozwolf.raml.generator.util.ClassPathUtils;

@JsonSerialize
@JsonPropertyOrder({"schema", "example"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlBodyModel {
    private final String contentType;
    private final String schema;
    private final String example;

    public RamlBodyModel(RamlBody annotation) {
        this.contentType = annotation.contentType();
        this.schema = generateSchema(annotation);
        this.example = generateExample(annotation);
    }

    RamlBodyModel(String contentType) {
        this.contentType = contentType;
        this.schema = null;
        this.example = null;
    }

    RamlBodyModel(String contentType, Class<?> type) {
        this.contentType = contentType;
        this.schema = RamlMedia.instance().generateSchemaFor(contentType, type).orElse(null);
        this.example = RamlMedia.instance().generateExampleFor(contentType, type).orElse(null);
    }

    @JsonIgnore
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("schema")
    public String getSchema() {
        return schema;
    }

    @JsonProperty("example")
    public String getExample() {
        return example;
    }

    private static String generateSchema(RamlBody annotation) {
        if (annotation.type() == RamlBody.NotDefinedReturnType.class)
            return ClassPathUtils.getResourceAsString(annotation.schema());

        return RamlMedia.instance().generateSchemaFor(annotation.contentType(), annotation.type())
                .orElseGet(() -> ClassPathUtils.getResourceAsString(annotation.schema()));
    }

    private static String generateExample(RamlBody annotation) {
        if (annotation.type() == RamlBody.NotDefinedReturnType.class)
            return ClassPathUtils.getResourceAsString(annotation.example());

        return RamlMedia.instance().generateExampleFor(annotation.contentType(), annotation.type())
                .orElseGet(() -> ClassPathUtils.getResourceAsString(annotation.example()));
    }
}
