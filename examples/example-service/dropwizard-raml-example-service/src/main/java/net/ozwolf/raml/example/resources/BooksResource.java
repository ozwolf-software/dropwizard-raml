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
@RamlSubResources({
        @RamlSubResource(path = @Path("/{id}"), description = "retrieve and update a book", uriParameters = @RamlParameter(name = "id", description = "the book id", type = "integer")),
        @RamlSubResource(path = @Path("/{id}/download"), description = "download a book", uriParameters = @RamlParameter(name = "id", description = "the book id", type = "integer"))
})
@Path("/books")
public class BooksResource {
    private final BookService bookService;

    public BooksResource() {
        this.bookService = BookService.INSTANCE;
    }

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
        return new BooksResponse(bookService.find(genre));
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
        BookAndAuthor b = bookService.create(request);
        return Response.created(UriBuilder.fromResource(BooksResource.class).path(BooksResource.class, "getBook").build(b.getId()))
                .entity(new BookResponse(b))
                .build();
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
        return bookService.find(id).map(BookResponse::new).orElseThrow(NotFoundException::new);
    }

    @RamlDescription("download a book in PDF format")
    @Path("/{id}/download")
    @GET
    @Produces("application/pdf")
    @Timed
    @Secured(bearers = false)
    public StreamingOutput getBookDownload(@PathParam("id") Integer id) {
        return output -> output.write(ExampleServiceApp.getSamplePDF());
    }
}
