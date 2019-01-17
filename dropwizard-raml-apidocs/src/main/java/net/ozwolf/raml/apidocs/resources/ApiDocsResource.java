package net.ozwolf.raml.apidocs.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.common.model.v08.V08_RamlApplication;
import net.ozwolf.raml.common.model.v10.V10_RamlApplication;
import net.ozwolf.raml.apidocs.views.ApiDocsView;
import net.ozwolf.raml.generator.RamlSpecification;
import org.raml.v2.api.RamlModelResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;

@Path("/apidocs")
public class ApiDocsResource {
    private final RamlSpecification specification;

    public ApiDocsResource(RamlSpecification specification) {
        this.specification = specification;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Timed
    public ApiDocsView getApplication() {
        RamlModelResult model = specification.getModel()
                .orElseThrow(() -> new ServiceUnavailableException("API specifications are currently unavailable.  Check logs for details."));

        if (model.isVersion10())
            return new ApiDocsView(new V10_RamlApplication(model.getApiV10()));

        if (model.isVersion08())
            return new ApiDocsView(new V08_RamlApplication(model.getApiV08()));

        throw new ServiceUnavailableException("Unsupported RAML version.");
    }
}
