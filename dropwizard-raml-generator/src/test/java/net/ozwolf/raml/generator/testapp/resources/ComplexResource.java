package net.ozwolf.raml.generator.testapp.resources;

import net.ozwolf.raml.annotations.RamlParameter;
import net.ozwolf.raml.annotations.RamlResource;
import net.ozwolf.raml.annotations.RamlUriParameters;

import javax.ws.rs.Path;

@RamlResource(displayName = "Complex", description = "a complex resource showcasing what the RAML generator can do")
@RamlUriParameters({
        @RamlParameter(name = "complexId", type = "string", description = "the complex identifier")
})
@Path("/complex/{complexId}")
public class ComplexResource {
}
