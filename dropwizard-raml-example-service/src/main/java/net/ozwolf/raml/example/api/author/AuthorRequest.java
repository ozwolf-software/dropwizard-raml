package net.ozwolf.raml.example.api.author;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.NotEmpty;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
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
}
