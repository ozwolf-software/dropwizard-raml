package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonPropertyOrder({"title", "content"})
public interface RamlDocumentation {
    @JsonProperty("title")
    String getTitle();

    @JsonProperty("content")
    String getContent();
}
