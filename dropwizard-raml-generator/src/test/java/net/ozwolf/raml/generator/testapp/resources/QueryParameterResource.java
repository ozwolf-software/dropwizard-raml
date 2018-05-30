package net.ozwolf.raml.generator.testapp.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.annotations.RamlDescription;
import net.ozwolf.raml.annotations.RamlResource;
import net.ozwolf.raml.generator.testapp.api.responses.SimpleResponse;
import net.ozwolf.raml.generator.testapp.domain.TestEnum;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import java.util.List;
import java.util.UUID;

@RamlResource(displayName = "Query Parameters", description = "resource testing the discovery of query parameters")
@Path("/query-parameters")
public class QueryParameterResource {
    @GET
    @Produces("application/json")
    @Timed
    public SimpleResponse getWithQueryParameters(@QueryParam("test1") @RamlDescription("the first test query param") String test1,
                                                 @QueryParam("test2") @NotNull Integer test2,
                                                 @QueryParam("test3") @DefaultValue("false") boolean test3,
                                                 @QueryParam("test4") List<String> test4,
                                                 @QueryParam("test5")List<TestEnum> test5) {
        return new SimpleResponse(UUID.randomUUID().toString());
    }
}
