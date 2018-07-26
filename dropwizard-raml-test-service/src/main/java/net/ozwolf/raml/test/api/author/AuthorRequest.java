package net.ozwolf.raml.test.api.author;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.annotations.RamlExample;

import javax.validation.constraints.NotEmpty;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSchemaTitle("Author Request")
@JsonSchemaDescription("a request to create or modify an author")
public class AuthorRequest {
    @NotEmpty(message = "must be provided")
    private final String name;

    @JsonCreator
    public AuthorRequest(@JsonProperty(value = "name", required = true)
                         @JsonPropertyDescription("the name of the author")
                                 String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @RamlExample
    public static AuthorRequest example() {
        return new AuthorRequest("Tom Clancy");
    }
}
