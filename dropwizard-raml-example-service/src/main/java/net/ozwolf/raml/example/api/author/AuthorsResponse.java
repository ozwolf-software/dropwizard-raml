package net.ozwolf.raml.example.api.author;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.example.core.domain.Author;
import net.ozwolf.raml.example.resources.AuthorsResource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@JsonSerialize
@JsonPropertyOrder({"self", "authors"})
@JsonSchemaTitle("Authors Response")
@JsonSchemaDescription("the collection of authors on record")
public class AuthorsResponse {
    private final URI self;
    private final List<AuthorReferenceResponse> authors;

    public AuthorsResponse(List<Author> authors){
        this.self = UriBuilder.fromResource(AuthorsResource.class).build();
        this.authors = authors.stream().map(AuthorReferenceResponse::new).collect(toList());
    }

    @JsonProperty(value = "self", required = true)
    @JsonPropertyDescription("the reference to this resource")
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "authors", required = true)
    @JsonPropertyDescription("the list of all authors")
    public List<AuthorReferenceResponse> getAuthors() {
        return authors;
    }
}
