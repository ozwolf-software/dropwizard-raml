package net.ozwolf.raml.generator.testapp.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.annotations.RamlResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.UUID;

@RamlResource(displayName = "Basic", description = "a basic resource for retrieving a random string")
@Path("/basic")
public class BasicResource {
    @GET
    @Produces("application/json")
    @Timed
    public Response getSimpleResponse() {
        return Response.ok().entity("{\"random\":\"" + UUID.randomUUID().toString() + "\"}").build();
    }
}
