package net.ozwolf.raml.example.api.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.example.api.author.AuthorReferenceResponse;
import net.ozwolf.raml.example.core.domain.Author;
import net.ozwolf.raml.example.core.domain.Book;
import net.ozwolf.raml.example.core.domain.BookAndAuthor;
import net.ozwolf.raml.example.core.domain.Genre;
import net.ozwolf.raml.example.resources.BooksResource;
import org.joda.time.LocalDate;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@JsonSerialize
@JsonPropertyOrder({"self", "download", "id", "title", "genre", "publishDate", "author"})
@JsonSchemaTitle("Book Response")
@JsonSchemaDescription("a book")
public class BookResponse {
    private final URI self;
    private final URI download;
    private final Integer id;
    private final String title;
    private final Genre genre;
    private final LocalDate publishDate;
    private final AuthorReferenceResponse author;

    public BookResponse(BookAndAuthor book){
        this.self = UriBuilder.fromResource(BooksResource.class).path(BooksResource.class, "getBook").build(book.getId());
        this.download = UriBuilder.fromResource(BooksResource.class).path(BooksResource.class, "getBookDownload").build(book.getId());
        this.id = book.getId();
        this.title = book.getTitle();
        this.genre = book.getGenre();
        this.publishDate = book.getPublishDate();
        this.author = new AuthorReferenceResponse(book.getAuthor());
    }

    @JsonProperty(value = "self", required = true)
    @JsonPropertyDescription("the reference to this resource")
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "download", required = true)
    @JsonPropertyDescription("the reference to the download resource")
    public URI getDownload() {
        return download;
    }

    @JsonProperty(value = "id", required = true)
    @JsonPropertyDescription("the book unique id")
    public Integer getId() {
        return id;
    }

    @JsonProperty(value = "title", required = true)
    @JsonPropertyDescription("the title of the book")
    public String getTitle() {
        return title;
    }

    @JsonProperty(value = "genre", required = true)
    @JsonPropertyDescription("the book genre")
    public Genre getGenre() {
        return genre;
    }

    @JsonProperty(value = "publishDate", required = true)
    @JsonPropertyDescription("the date the book was published")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSchemaFormat("yyyy-MM-dd")
    public LocalDate getPublishDate() {
        return publishDate;
    }

    @JsonProperty(value = "author", required = true)
    @JsonPropertyDescription("the author of the book")
    public AuthorReferenceResponse getAuthor() {
        return author;
    }

    @RamlExample
    public static BookResponse example(){
        return new BookResponse(
                new BookAndAuthor(
                        new Book(
                                1,
                                "Book 1: How to RAML",
                                Genre.NonFiction,
                                LocalDate.parse("2018-05-20"),
                                1
                        ),
                        new Author(
                                1,
                                "John Smith",
                                LocalDate.parse("2018-01-01")
                        )
                )
        );
    }
}
