package net.ozwolf.raml.test.api.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.test.core.domain.Book;
import net.ozwolf.raml.test.resources.BooksResource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@JsonSerialize
@JsonPropertyOrder({"href", "id", "title"})
@JsonSchemaTitle("Book Reference Response")
@JsonSchemaDescription("a reference to an author resource")
public class BookReferenceResponse {
    private final URI href;
    private final Integer id;
    private final String title;

    public BookReferenceResponse(Book book) {
        this.href = UriBuilder.fromResource(BooksResource.class).path(BooksResource.class, "getBook").build(book.getId());
        this.id = book.getId();
        this.title = book.getTitle();
    }

    @JsonProperty(value = "href", required = true)
    @JsonPropertyDescription("the reference to the book resource")
    public URI getHref() {
        return href;
    }

    @JsonProperty(value = "id", required = true)
    @JsonPropertyDescription("the book's id")
    public Integer getId() {
        return id;
    }

    @JsonProperty(value = "title", required = true)
    @JsonPropertyDescription("the book's title")
    public String getTitle() {
        return title;
    }
}
