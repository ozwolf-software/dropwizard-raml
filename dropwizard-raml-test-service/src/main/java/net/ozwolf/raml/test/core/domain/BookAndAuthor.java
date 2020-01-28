package net.ozwolf.raml.test.core.domain;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class BookAndAuthor {
    private final Book book;
    private final Author author;

    public BookAndAuthor(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

    public Integer getId() {
        return book.getId();
    }

    public String getTitle() {
        return book.getTitle();
    }

    public Genre getGenre() {
        return book.getGenre();
    }

    public LocalDate getPublishDate() {
        return book.getPublishDate();
    }

    public Author getAuthor() {
        return author;
    }

    public BigDecimal getRrp() { return book.getRrp(); }
}
