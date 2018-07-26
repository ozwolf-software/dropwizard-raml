package net.ozwolf.raml.test.core.service;

import net.ozwolf.raml.test.api.author.AuthorRequest;
import net.ozwolf.raml.test.core.domain.Author;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

public class AuthorService {
    private final List<Author> authors;

    public AuthorService() {
        this.authors = newArrayList(
                new Author(
                        new AuthorRequest("John Smith")
                ),
                new Author(
                        new AuthorRequest("Mary Robinson")
                )
        );
    }

    public List<Author> findAll() {
        return authors;
    }

    public Optional<Author> find(Integer id) {
        return authors.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Author create(AuthorRequest request) {
        Author author = new Author(request);
        this.authors.add(author);
        return author;
    }

    public Optional<Author> update(Integer id, AuthorRequest request) {
        return find(id).map(a -> a.update(request));
    }

    public final static AuthorService INSTANCE = new AuthorService();
}
