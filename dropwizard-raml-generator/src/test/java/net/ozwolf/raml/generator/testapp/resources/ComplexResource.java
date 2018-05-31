package net.ozwolf.raml.generator.testapp.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.generator.testapp.api.responses.ComplexResponse;
import net.ozwolf.raml.generator.testapp.api.responses.PersonResponse;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;

@RamlResource(displayName = "Complex", description = "a complex resource showcasing what the RAML generator can do")
@RamlUriParameters({
        @RamlParameter(name = "complexId", type = "string", description = "the complex identifier")
})
@RamlSubResources({
        @RamlSubResource(path = @Path("/person/{personId}"), uriParameters = @RamlParameter(name = "personId", type = "string", description = "the person identifier"))
})
@Path("/complex/{complexId}")
public class ComplexResource {
    @RamlSecuredBy("custom-token")
    @RamlTraits("has404")
    @RamlDescription("resource to return a complex object")
    @RamlResponses(
            @RamlResponse(
                    status = 200,
                    description = "the complex object was successfully found",
                    bodies = @RamlBody(
                            contentType = "application/json",
                            type = ComplexResponse.class
                    )
            )
    )
    @GET
    @Produces("application/json")
    @Timed
    public ComplexResponse getComplex(@PathParam("complexId") @NotNull String id,
                                      @QueryParam("date-for") @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}") String dateFor,
                                      @HeaderParam("x-test-context") @DefaultValue("test") String testContext) {
        return ComplexResponse.example();
    }

    @RamlSecuredBy("custom-token")
    @RamlTraits("has404")
    @RamlDescription("resource to return a person")
    @RamlResponses(
            @RamlResponse(
                    status = 200,
                    description = "person successfully found",
                    bodies = @RamlBody(
                            contentType = "application/json",
                            type = PersonResponse.class
                    )
            )
    )
    @Path("/person/{personId}")
    @GET
    @Produces("application/json")
    @Timed
    public PersonResponse getPerson(@PathParam("complexId") @NotNull String complexId,
                                    @PathParam("personId") @NotNull String personId) {
        return PersonResponse.example();
    }
}
