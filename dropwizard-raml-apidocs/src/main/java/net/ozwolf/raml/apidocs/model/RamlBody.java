package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonPropertyOrder({"contentType", "schema", "example"})
public interface RamlBody {
    @JsonProperty("contentType")
    String getContentType();
    @JsonProperty("schema")
    String getSchema();
    @JsonProperty("example")
    String getExample();
}
