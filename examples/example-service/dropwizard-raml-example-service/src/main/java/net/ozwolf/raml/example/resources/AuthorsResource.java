package net.ozwolf.raml.example.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.example.api.author.AuthorRequest;
import net.ozwolf.raml.example.api.author.AuthorResponse;
import net.ozwolf.raml.example.api.author.AuthorsResponse;
import net.ozwolf.raml.example.security.Secured;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@RamlResource(displayName = "Authors", description = "manage and retrieve authors")
@RamlSubResources(
        @RamlSubResource(path = @Path("/{id}"), description = "retrieve and update an author", uriParameters = @RamlParameter(name = "id", description = "the author id", type = "integer"))
)
@Path("/authors")
public class AuthorsResource {
    @RamlDescription("retrieve the list of authors")
    @RamlResponses(
            @RamlResponse(status = 200, description = "authors retrieved successfully", bodies = @RamlBody(contentType = "application/json", type = AuthorsResponse.class))
    )
    @GET
    @Produces("application/json")
    @Timed
    public AuthorsResponse getAuthors() {
        return null;
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
        return null;
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
    @GET
    @Produces("application/json")
    @Timed
    public AuthorResponse getAuthor(@PathParam("id") Integer id) {
        return null;
    }

    @RamlDescription("update an author")
    @RamlRequests({
            @RamlBody(contentType = "application/json", type = AuthorRequest.class)
    })
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
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @Timed
    @Secured
    public AuthorResponse putAuthor(@PathParam("id") Integer id,
                                    @Valid AuthorRequest request) {
        return null;
    }
}
