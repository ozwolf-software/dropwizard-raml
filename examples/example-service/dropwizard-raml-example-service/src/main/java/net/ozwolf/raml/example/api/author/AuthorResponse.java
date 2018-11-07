package net.ozwolf.raml.example.api.author;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.example.api.book.BookReferenceResponse;
import net.ozwolf.raml.example.core.domain.Author;
import net.ozwolf.raml.example.core.domain.Book;
import net.ozwolf.raml.example.core.domain.Genre;
import net.ozwolf.raml.example.resources.AuthorsResource;
import org.joda.time.LocalDate;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@JsonSerialize
@JsonPropertyOrder({"self", "id", "name", "firstPublished", "bibliography"})
@JsonSchemaTitle("Author Response")
@JsonSchemaDescription("an author")
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
    @JsonPropertyDescription("the reference to this resource")
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "id", required = true)
    @JsonPropertyDescription("the author unique identifier")
    public Integer getId() {
        return id;
    }

    @JsonProperty(value = "name", required = true)
    @JsonPropertyDescription("the author's name")
    public String getName() {
        return name;
    }

    @JsonProperty("firstPublished")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSchemaFormat("yyyy-MM-dd")
    @JsonPropertyDescription("the date the author was first published")
    public LocalDate getFirstPublished() {
        return firstPublished;
    }

    @JsonProperty(value = "bibliography", required = true)
    @JsonPropertyDescription("the bibliography of the author")
    public List<BookReferenceResponse> getBooks() {
        return books;
    }

    @RamlExample
    public static AuthorResponse example(){
        return new AuthorResponse(
                new Author(
                        1,
                        "John Smith",
                        LocalDate.parse("2018-01-01")
                ),
                newArrayList(
                        new Book(
                                1,
                                "My First Book",
                                Genre.Action,
                                LocalDate.parse("2018-01-01"),
                                1
                        ),
                        new Book(
                                2,
                                "Beyond",
                                Genre.SciFi,
                                LocalDate.parse("2018-05-20"),
                                1
                        )
                )
        );
    }
}
