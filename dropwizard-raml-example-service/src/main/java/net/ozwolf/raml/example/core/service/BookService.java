package net.ozwolf.raml.example.core.service;

import net.ozwolf.raml.example.api.book.BookRequest;
import net.ozwolf.raml.example.core.domain.Book;
import net.ozwolf.raml.example.core.domain.BookAndAuthor;
import net.ozwolf.raml.example.core.domain.Genre;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class BookService {
    private final AuthorService authorService;
    private final List<Book> books;

    public BookService() {
        this.authorService = AuthorService.INSTANCE;
        this.books = newArrayList(
                new Book(
                        new BookRequest(
                                "Book 1: How to RAML",
                                Genre.NonFiction,
                                LocalDate.parse("2018-01-01"),
                                1
                        )
                ),
                new Book(
                        new BookRequest(
                                "Alpha Conspiracy",
                                Genre.Action,
                                LocalDate.parse("2017-08-12"),
                                2
                        )
                )
        );
    }

    public List<Book> find(List<Genre> genres){
        return books.stream().filter(b -> genres.isEmpty() || genres.stream().anyMatch(g -> g == b.getGenre())).collect(toList());
    }

    public Optional<BookAndAuthor> find(Integer id) {
        return books.stream().filter(i -> i.getId().equals(id)).findFirst()
                .map(b -> new BookAndAuthor(b, authorService.find(b.getAuthorId()).orElseThrow(() -> new IllegalStateException("Unknown author [ " + b.getAuthorId() + " ]"))));
    }

    public List<Book> findByAuthor(Integer authorId) {
        return books.stream().filter(i -> i.getAuthorId().equals(authorId)).collect(toList());
    }

    public BookAndAuthor create(BookRequest request) {
        Book book = new Book(request);
        books.add(book);
        return new BookAndAuthor(book, authorService.find(book.getAuthorId()).map(a -> a.withPublishDate(book.getPublishDate())).orElseThrow(() -> new IllegalStateException("Unknown author [ " + book.getAuthorId() + " ]")));
    }

    public final static BookService INSTANCE = new BookService();
}
