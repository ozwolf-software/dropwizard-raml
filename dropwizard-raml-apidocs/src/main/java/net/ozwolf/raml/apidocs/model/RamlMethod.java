package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonPropertyOrder({"method", "description", "headers", "queryParameters", "requests", "responses", "security"})
public interface RamlMethod {
    @JsonProperty("method")
    String getMethod();

    @JsonProperty("description")
    String getDescription();

    @JsonProperty("headers")
    List<RamlParameter> getHeaders();

    @JsonProperty("queryParameters")
    List<RamlParameter> getQueryParameters();

    @JsonProperty("requests")
    List<RamlBody> getRequests();

    @JsonProperty("responses")
    List<RamlResponse> getResponses();

    @JsonProperty("security")
    List<RamlSecurity> getSecurity();
}
