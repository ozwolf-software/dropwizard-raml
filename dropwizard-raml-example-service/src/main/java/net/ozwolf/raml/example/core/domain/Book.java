package net.ozwolf.raml.example.core.domain;

import net.ozwolf.raml.example.api.book.BookRequest;
import org.joda.time.LocalDate;

import java.util.concurrent.atomic.AtomicInteger;

public class Book {
    private final Integer id;
    private final String title;
    private final Genre genre;
    private final LocalDate publishDate;
    private final Integer authorId;

    public Book(Integer id, String title, Genre genre, LocalDate publishDate, Integer authorId) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.publishDate = publishDate;
        this.authorId = authorId;
    }

    public Book(BookRequest request) {
        this(
                getNextId(),
                request.getTitle(),
                request.getGenre(),
                request.getPublishDate(),
                request.getAuthorId()
        );
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

    public Integer getAuthorId() {
        return authorId;
    }

    private final static AtomicInteger NEXT_ID = new AtomicInteger(1);

    private static synchronized Integer getNextId() {
        return NEXT_ID.getAndIncrement();
    }
}
