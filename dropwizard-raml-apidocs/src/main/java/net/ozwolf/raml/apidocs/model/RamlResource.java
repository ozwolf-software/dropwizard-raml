package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonPropertyOrder({"displayName", "description", "path", "uriParameters", "methods", "resources"})
public interface RamlResource {
    @JsonProperty("displayName")
    String getDisplayName();

    @JsonProperty("description")
    String getDescription();

    @JsonProperty("path")
    String getPath();

    @JsonProperty("uriParameters")
    List<RamlParameter> getUriParameters();

    @JsonProperty("methods")
    List<RamlMethod> getMethods();

    @JsonProperty("resources")
    List<RamlResource> getResources();
}
