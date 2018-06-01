package net.ozwolf.raml.example.api.author;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import net.ozwolf.raml.example.api.book.BookReferenceResponse;
import net.ozwolf.raml.example.core.domain.Author;
import net.ozwolf.raml.example.core.domain.Book;
import net.ozwolf.raml.example.resources.AuthorsResource;
import org.joda.time.LocalDate;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@JsonSerialize
@JsonPropertyOrder({"self", "id", "name", "firstPublished", "books"})
public class AuthorResponse {
    private final URI self;
    private final Integer id;
    private final String name;
    private final LocalDate firstPublished;
    private final List<BookReferenceResponse> books;

    public AuthorResponse(Author author, List<Book> books){
        this.self = UriBuilder.fromResource(AuthorsResource.class).path(AuthorsResource.class, "getAuthor").build(author.getId());
        this.id = author.getId();
        this.name = author.getName();
        this.firstPublished = author.getFirstPublishedDate().orElse(null);
        this.books = books.stream().map(BookReferenceResponse::new).collect(toList());
    }

    public AuthorResponse(Author author){
        this(author, newArrayList());
    }

    @JsonProperty(value = "self", required = true)
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "id", required = true)
    public Integer getId() {
        return id;
    }

    @JsonProperty(value = "name", required = true)
    public String getName() {
        return name;
    }

    @JsonProperty(value = "firstPublished", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSchemaFormat("yyyy-MM-dd")
    public LocalDate getFirstPublished() {
        return firstPublished;
    }

    @JsonProperty(value = "books", required = true)
    public List<BookReferenceResponse> getBooks() {
        return books;
    }
}
