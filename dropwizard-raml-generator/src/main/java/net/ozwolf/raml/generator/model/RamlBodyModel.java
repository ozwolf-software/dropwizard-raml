package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.generator.media.SupportedMediaType;

@JsonSerialize
@JsonPropertyOrder({"schema", "example"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlBodyModel {
    private final String contentType;
    private final SchemaAndExample schemaAndExample;

    public RamlBodyModel(RamlBody annotation) {
        this.contentType = annotation.contentType();
        this.schemaAndExample = SupportedMediaType.getSchemaAndExample(annotation);
    }

    RamlBodyModel(String contentType){
        this.contentType = contentType;
        this.schemaAndExample = SchemaAndExample.NONE;
    }

    RamlBodyModel(String contentType, Class<?> type){
        this.contentType = contentType;
        this.schemaAndExample = SupportedMediaType.getSchemaAndExample(contentType, type);
    }

    @JsonIgnore
    public String getContentType(){
        return contentType;
    }

    @JsonProperty("schema")
    public String getSchema() {
        return schemaAndExample.getSchema().orElse(null);
    }

    @JsonProperty("example")
    public String getExample() {
        return schemaAndExample.getExample().orElse(null);
    }
}
