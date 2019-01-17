package net.ozwolf.raml.test.api.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.annotations.RamlSchema;
import net.ozwolf.raml.test.core.domain.Genre;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

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

    public String getT() {
        return title;
    }

    public Genre getG() {
        return genre;
    }

    public LocalDate getP() {
        return publishDate;
    }

    public Integer getA() {
        return authorId;
    }

    @RamlExample
    public static BookRequest example() {
        return new BookRequest(
                "Beyond the Stars",
                Genre.SciFi,
                LocalDate.parse("2017-06-01"),
                1
        );
    }
}
