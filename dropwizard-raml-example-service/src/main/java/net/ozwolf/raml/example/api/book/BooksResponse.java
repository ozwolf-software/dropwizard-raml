package net.ozwolf.raml.example.api.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.example.core.domain.Book;
import net.ozwolf.raml.example.core.domain.Genre;
import net.ozwolf.raml.example.resources.BooksResource;
import org.joda.time.LocalDate;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@JsonSerialize
@JsonPropertyOrder({"self", "books"})
public class BooksResponse {
    private final URI self;
    private final List<BookReferenceResponse> books;

    public BooksResponse(List<Book> books){
        this.self = UriBuilder.fromResource(BooksResource.class).build();
        this.books = books.stream().map(BookReferenceResponse::new).collect(toList());
    }

    @JsonProperty(value = "self", required = true)
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "books", required = true)
    public List<BookReferenceResponse> getBooks() {
        return books;
    }

    @RamlExample
    public static BooksResponse example(){
        return new BooksResponse(
                newArrayList(
                        new Book(
                                1,
                                "Book 1: How to RAML",
                                Genre.NonFiction,
                                LocalDate.parse("2018-01-01"),
                                1
                        ),
                        new Book(
                                1,
                                "Humanity Lost",
                                Genre.SciFi,
                                LocalDate.parse("2018-01-01"),
                                1
                        )
                )
        );
    }
}
