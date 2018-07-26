package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Set;

@JsonSerialize
@JsonPropertyOrder({"title", "version", "description", "protocols", "resources"})
public interface RamlApplication {
    @JsonProperty("title")
    String getTitle();
    @JsonProperty("version")
    String getVersion();
    @JsonProperty("description")
    String getDescription();
    @JsonProperty("protocols")
    Set<String> getProtocols();

    @JsonProperty("resources")
    List<RamlResource> getResources();
}
