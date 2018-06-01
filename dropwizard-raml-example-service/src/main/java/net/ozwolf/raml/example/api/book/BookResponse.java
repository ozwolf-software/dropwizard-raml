package net.ozwolf.raml.example.api.book;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.example.api.author.AuthorReferenceResponse;
import net.ozwolf.raml.example.core.domain.BookAndAuthor;
import net.ozwolf.raml.example.core.domain.Genre;
import net.ozwolf.raml.example.resources.BooksResource;
import org.joda.time.LocalDate;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@JsonSerialize
@JsonPropertyOrder({"self", "download", "id", "title", "genre", "publishDate", "author"})
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

    public URI getSelf() {
        return self;
    }

    public URI getDownload() {
        return download;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public AuthorReferenceResponse getAuthor() {
        return author;
    }
}
