package net.ozwolf.raml.example.core.domain;

import net.ozwolf.raml.example.api.author.AuthorRequest;
import org.joda.time.LocalDate;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Sets.newHashSet;

public class Author {
    private final Integer id;
    private String name;
    private final LocalDate firstPublishedDate;

    public Author(Integer id,
                  String name,
                  LocalDate firstPublishedDate) {
        this.id = id;
        this.name = name;
        this.firstPublishedDate = firstPublishedDate;
    }

    public Author(AuthorRequest request) {
        this(
                getNextId(),
                request.getName(),
                null
        );
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<LocalDate> getFirstPublishedDate() {
        return Optional.ofNullable(firstPublishedDate);
    }

    public Author update(AuthorRequest request){
        this.name = request.getName();
        return this;
    }

    private final static AtomicInteger NEXT_ID = new AtomicInteger(1);

    private static synchronized Integer getNextId() {
        return NEXT_ID.getAndIncrement();
    }
}
