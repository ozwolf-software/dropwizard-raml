package net.ozwolf.raml.test.resources;

import net.ozwolf.raml.annotations.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@RamlResource(displayName = "Publishers", description = "manage and retrieve publishers")
@RamlUriParameters({
        @RamlParameter(name = "id", type = "string", description = "the publisher identifier")
})
@RamlSubResources({
        @RamlSubResource(path = @Path("/books"), description = "retrieve and manage books for a publisher")
})
@Path("/publishers/{id}")
public class PublishersResource {
    @RamlDescription("retrieve books for a given publisher")
    @RamlResponses({
            @RamlResponse(
                    status = 200,
                    description = "books returned successfully",
                    bodies = @RamlBody(
                            contentType = "application/json",
                            example = "[{\"title\":\"Something something something, Dark Size\"}]"
                    )
            )
    })
    @Path("/books")
    @GET
    @Produces("application/json")
    public Response getBooks(@PathParam("id") String id) {
        return Response.ok("[]").build();
    }
}
