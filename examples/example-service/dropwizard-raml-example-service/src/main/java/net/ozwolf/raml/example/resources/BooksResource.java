package net.ozwolf.raml.example.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.example.ExampleServiceApp;
import net.ozwolf.raml.example.api.book.BookRequest;
import net.ozwolf.raml.example.api.book.BookResponse;
import net.ozwolf.raml.example.api.book.BooksResponse;
import net.ozwolf.raml.example.core.domain.BookAndAuthor;
import net.ozwolf.raml.example.core.domain.Genre;
import net.ozwolf.raml.example.core.service.BookService;
import net.ozwolf.raml.example.security.Secured;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@RamlResource(displayName = "Books", description = "manage books")
@RamlUriParameters(
        @RamlParameter(name = "id", description = "the author id", type = "integer")
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
        return null;
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
        return null;
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
