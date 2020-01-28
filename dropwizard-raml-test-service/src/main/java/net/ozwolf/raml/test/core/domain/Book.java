package net.ozwolf.raml.test.core.domain;

import net.ozwolf.raml.test.api.book.BookRequest;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Book {
    private final Integer id;
    private final String title;
    private final Genre genre;
    private final LocalDate publishDate;
    private final Integer authorId;
    private final BigDecimal rrp;

    public Book(Integer id, String title, Genre genre, LocalDate publishDate, Integer authorId, BigDecimal rrp) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.publishDate = publishDate;
        this.authorId = authorId;
        this.rrp = rrp;
    }

    public Book(BookRequest request) {
        this(
                getNextId(),
                request.getT(),
                request.getG(),
                request.getP(),
                request.getA(),
                request.getR()
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

    public BigDecimal getRrp() {
        return rrp;
    }

    private final static AtomicInteger NEXT_ID = new AtomicInteger(1);

    private static synchronized Integer getNextId() {
        return NEXT_ID.getAndIncrement();
    }
}
