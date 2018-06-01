package net.ozwolf.raml.example.api.author;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.net.URI;
import java.util.List;

@JsonSerialize
@JsonPropertyOrder({"self", "authors"})
public class AuthorsResponse {
    private final URI self;
    private final List<AuthorReferenceResponse> authors;
}
