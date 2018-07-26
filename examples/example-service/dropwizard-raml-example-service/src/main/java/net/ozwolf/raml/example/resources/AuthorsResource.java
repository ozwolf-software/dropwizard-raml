package net.ozwolf.raml.test.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.test.api.author.AuthorRequest;
import net.ozwolf.raml.test.api.author.AuthorResponse;
import net.ozwolf.raml.test.api.author.AuthorsResponse;
import net.ozwolf.raml.test.core.domain.Author;
import net.ozwolf.raml.test.core.service.AuthorService;
import net.ozwolf.raml.test.core.service.BookService;
import net.ozwolf.raml.test.security.Secured;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@RamlResource(displayName = "Authors", description = "manage and retrieve authors")
@RamlSubResources(
        @RamlSubResource(path = @Path("/{id}"), description = "retrieve and update an author", uriParameters = @RamlParameter(name = "id", description = "the author id", type = "integer"))
)
@Path("/authors")
public class AuthorsResource {
    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorsResource() {
        this.authorService = AuthorService.INSTANCE;
        this.bookService = BookService.INSTANCE;
    }

    @RamlDescription("retrieve the list of authors")
    @RamlResponses(
            @RamlResponse(status = 200, description = "authors retrieved successfully", bodies = @RamlBody(contentType = "application/json", type = AuthorsResponse.class))
    )
    @GET
    @Produces("application/json")
    @Timed
    public AuthorsResponse getAuthors() {
        return new AuthorsResponse(authorService.findAll());
    }

    @RamlDescription("create a new author")
    @RamlResponses(
            @RamlResponse(
                    status = 201,
                    description = "authors created successfully",
                    headers = @RamlParameter(name = "Location", description = "the location of the newly created resource", type = "string"),
                    bodies = @RamlBody(contentType = "application/json", type = AuthorResponse.class)
            )
    )
    @RamlRequests(
            @RamlBody(contentType = "application/json", type = AuthorRequest.class)
    )
    @RamlSecuredBy("oauth2")
    @RamlIs({"has400", "validated"})
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Timed
    @Secured(users = false)
    public Response postAuthor(@Valid AuthorRequest request) {
        Author a = authorService.create(request);
        return Response.created(UriBuilder.fromResource(AuthorsResource.class).path(AuthorsResource.class, "getAuthor").build(a.getId()))
                .entity(new AuthorResponse(a))
                .build();
    }

    @RamlDescription("retrieve an author")
    @RamlResponses(
            @RamlResponse(
                    status = 200,
                    description = "author retrieved successfully",
                    bodies = @RamlBody(contentType = "application/json", type = AuthorResponse.class)
            )
    )
    @RamlIs("has404")
    @Path("/{id}")
    @Produces("application/json")
    @Timed
    public AuthorResponse getAuthor(@PathParam("id") Integer id) {
        return authorService.find(id)
                .map(a -> new AuthorResponse(a, bookService.findByAuthor(a.getId())))
                .orElseThrow(NotFoundException::new);
    }

    @RamlDescription("update an author")
    @RamlResponses(
            @RamlResponse(
                    status = 200,
                    description = "authors updated successfully",
                    bodies = @RamlBody(contentType = "application/json", type = AuthorResponse.class)
            )
    )
    @RamlSecuredBy({"oauth2", "user-token"})
    @RamlIs({"has400", "validated"})
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Timed
    @Secured
    public AuthorResponse putAuthor(@PathParam("id") Integer id,
                                    @Valid AuthorRequest request) {
        return authorService.update(id, request)
                .map(a -> new AuthorResponse(a, bookService.findByAuthor(a.getId())))
                .orElseThrow(NotFoundException::new);
    }
}
