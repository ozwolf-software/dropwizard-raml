package net.ozwolf.raml.test.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.test.api.book.BookRequest;
import net.ozwolf.raml.test.api.book.BookResponse;
import net.ozwolf.raml.test.api.book.BooksResponse;
import net.ozwolf.raml.test.core.domain.Book;
import net.ozwolf.raml.test.core.domain.Genre;
import net.ozwolf.raml.test.security.Secured;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RamlResource(displayName = "Books", description = "manage books")
@RamlUriParameters(
        @RamlParameter(name = "id", description = "the book id", type = "integer")
)
@RamlSubResources({
        @RamlSubResource(path = @Path("/{id}"), description = "retrieve and update a book"),
        @RamlSubResource(path = @Path("/{id}/download"), description = "download a book")
})
@Path("/books")
public class BooksResource {
    @RamlDescription("retrieve the list of books")
    @RamlResponses(
            @RamlResponse(
                    status = 200,
                    description = "books retrieved successfully",
                    bodies = @RamlBody(contentType = "application/json", type = BooksResponse.class)
            )
    )
    @GET
    @Produces("application/json")
    @Timed
    public BooksResponse getBooks(@QueryParam("genre") List<Genre> genre) {
        return new BooksResponse(
                newArrayList(
                        new Book(1, "Supernova", Genre.SciFi, LocalDate.parse("2018-12-11"), 1),
                        new Book(2, "The Life and Times of Stephen Hawking", Genre.NonFiction, LocalDate.parse("2018-12-18"), 2)
                )
        );
    }

    @RamlDescription("create a new book")
    @RamlResponses(
            @RamlResponse(
                    status = 201,
                    description = "book created successfully",
                    headers = @RamlParameter(name = "Location", description = "the location of the newly created resource", type = "string"),
                    bodies = @RamlBody(contentType = "application/json", type = BookResponse.class)
            )
    )
    @RamlRequests(
            @RamlBody(contentType = "application/json", type = BookRequest.class)
    )
    @RamlSecuredBy("oauth2")
    @RamlIs({"has400", "validated"})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Timed
    public Response postBook(@Valid BookRequest request) {
        return Response.status(201).entity("{}").build();
    }

    @RamlDescription("retrieve a book")
    @RamlResponses(
            @RamlResponse(
                    status = 200,
                    description = "book retrieved successfully",
                    bodies = @RamlBody(contentType = "application/json", type = BookResponse.class)
            )
    )
    @Path("/{id}")
    @GET
    @Produces("application/json")
    @Timed
    public BookResponse getBook(@PathParam("id") Integer id) {
        return null;
    }

    @RamlDescription("download a book in PDF format")
    @RamlSecuredBy({"user-token", "oauth2"})
    @Path("/{id}/download")
    @GET
    @Produces("application/pdf")
    @Timed
    @Secured(bearers = false)
    public StreamingOutput getBookDownload(@PathParam("id") Integer id) {
        return null;
    }
}
