package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonPropertyOrder({"name", "type", "description", "headers", "queryParameters", "responses"})
public interface RamlSecurity {
    @JsonProperty("name")
    String getName();
    @JsonProperty("type")
    String getType();
    @JsonProperty("description")
    String getDescription();
    @JsonProperty("headers")
    List<RamlParameter> getHeaders();
    @JsonProperty("queryParameters")
    List<RamlParameter> getQueryParameters();
    @JsonProperty("responses")
    List<RamlResponse> getResponses();
}
