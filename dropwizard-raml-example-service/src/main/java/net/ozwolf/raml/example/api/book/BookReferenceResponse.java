package net.ozwolf.raml.example.api.book;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.example.core.domain.Book;
import net.ozwolf.raml.example.resources.BooksResource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@JsonSerialize
@JsonPropertyOrder({"href", "id", "title"})
public class BookReferenceResponse {
    private final URI href;
    private final Integer id;
    private final String title;

    public BookReferenceResponse(Book book) {
        this.href = UriBuilder.fromResource(BooksResource.class).path(BooksResource.class, "getBook").build(book.getId());
        this.id = book.getId();
        this.title = book.getTitle();
    }

    public URI getHref() {
        return href;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
