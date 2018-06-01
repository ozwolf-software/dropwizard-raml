package net.ozwolf.raml.example.api.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.ozwolf.raml.example.api.validation.ValidAuthor;
import net.ozwolf.raml.example.core.domain.Genre;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
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
    public BookRequest(@JsonProperty(value = "title", required = true)
                       @JsonPropertyDescription("the title of the book")
                               String title,
                       @JsonProperty(value = "genre", required = true)
                       @JsonPropertyDescription("the genre of the book")
                               Genre genre,
                       @JsonProperty(value = "publishDate", required = true)
                       @JsonPropertyDescription("the date the book was published")
                               LocalDate publishDate,
                       @JsonProperty(value = "authorId", required = true)
                       @JsonPropertyDescription("the id of the author")
                               Integer authorId) {
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
