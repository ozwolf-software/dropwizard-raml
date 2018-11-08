package net.ozwolf.raml.test.api.author;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.test.core.domain.Author;
import net.ozwolf.raml.test.resources.AuthorsResource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@JsonSerialize
@JsonPropertyOrder({"href", "id", "name"})
@JsonSchemaTitle("Author Resource Reference")
@JsonSchemaDescription("a reference to an author resource")
public class AuthorReferenceResponse {
    private final URI href;
    private final Integer id;
    private final String name;

    public AuthorReferenceResponse(Author author) {
        this.href = UriBuilder.fromResource(AuthorsResource.class).path(AuthorsResource.class, "getAuthor").build(author.getId());
        this.id = author.getId();
        this.name = author.getName();
    }

    @JsonProperty(value = "href", required = true)
    @JsonPropertyDescription("the reference to the author resource")
    public URI getHref() {
        return href;
    }

    @JsonProperty(value = "id", required = true)
    @JsonPropertyDescription("the author identifier")
    public Integer getId() {
        return id;
    }

    @JsonProperty(value = "name", required = true)
    @JsonPropertyDescription("the name of the author")
    public String getName() {
        return name;
    }
}
