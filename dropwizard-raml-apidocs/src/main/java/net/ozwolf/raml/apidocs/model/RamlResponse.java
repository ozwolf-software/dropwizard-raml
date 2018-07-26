package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonPropertyOrder({"status", "description", "headers", "bodies"})
public interface RamlResponse {
    @JsonProperty("status")
    Integer getStatus();

    @JsonProperty("description")
    String getDescription();

    @JsonProperty("headers")
    List<RamlParameter> getHeaders();

    @JsonProperty("bodies")
    List<RamlBody> getBodies();
}
