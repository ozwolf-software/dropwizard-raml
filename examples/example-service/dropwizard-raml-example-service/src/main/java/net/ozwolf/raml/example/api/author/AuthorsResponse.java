package net.ozwolf.raml.test.api.author;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.test.core.domain.Author;
import net.ozwolf.raml.test.resources.AuthorsResource;
import org.joda.time.LocalDate;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
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

    @RamlExample
    public static AuthorsResponse example(){
        return new AuthorsResponse(
                newArrayList(
                        new Author(
                                1,
                                "John Smith",
                                LocalDate.parse("2018-01-01")
                        ),
                        new Author(
                                2,
                                "Joan Valiant",
                                null
                        )
                )
        );
    }
}
