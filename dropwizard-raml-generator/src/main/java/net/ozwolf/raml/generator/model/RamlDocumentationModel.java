package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlDocumentation;
import net.ozwolf.raml.generator.util.ResourceUtils;

@JsonSerialize
@JsonPropertyOrder({"title", "content"})
public class RamlDocumentationModel {
    private final String title;
    private final String content;

    public RamlDocumentationModel(RamlDocumentation annotation) {
        this.title = annotation.title();
        this.content = ResourceUtils.getResourceAsString(annotation.content());
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }
}
