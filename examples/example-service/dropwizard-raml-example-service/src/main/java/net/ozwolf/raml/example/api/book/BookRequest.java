package net.ozwolf.raml.example.api.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.annotations.RamlSchema;
import net.ozwolf.raml.example.api.validation.ValidAuthor;
import net.ozwolf.raml.example.core.domain.Genre;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@RamlSchema("apispecs/resources/requests/schemas/book-request.json")
@RamlExample("apispecs/resources/requests/examples/book-request.json")
public class BookRequest {
    @NotEmpty(message = "must be provided")
    private final String title;
    @NotNull(message = "must be provided")
    private final Genre genre;
    @NotNull(message = "must be provided")
    private final LocalDate publishDate;
    @NotNull(message = "must be provided")
    @ValidAuthor
    private final Integer authorId;

    @JsonCreator
    public BookRequest(@JsonProperty("title") String title,
                       @JsonProperty("genre") Genre genre,
                       @JsonProperty("publishDate") LocalDate publishDate,
                       @JsonProperty("authorId") Integer authorId) {
        this.title = title;
        this.genre = genre;
        this.publishDate = publishDate;
        this.authorId = authorId;
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
}
