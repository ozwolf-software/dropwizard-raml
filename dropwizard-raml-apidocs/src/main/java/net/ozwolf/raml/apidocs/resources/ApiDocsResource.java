package net.ozwolf.raml.apidocs.resources;

import com.codahale.metrics.annotation.Timed;
import net.ozwolf.raml.apidocs.model.v08.V08_RamlApplication;
import net.ozwolf.raml.apidocs.model.v10.V10_RamlApplication;
import net.ozwolf.raml.apidocs.views.ApiDocsView;
import org.raml.v2.api.RamlModelResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;

@Path("/apidocs")
public class ApiDocsResource {
    private final RamlModelResult model;

    public ApiDocsResource(RamlModelResult model) {
        this.model = model;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Timed
    public ApiDocsView getApplication() {
        if (model == null)
            throw new ServiceUnavailableException("API specifications are currently unavailable.  Check logs for details.");

        if (model.isVersion10())
            return new ApiDocsView(new V10_RamlApplication(model.getApiV10()));

        if (model.isVersion08())
            return new ApiDocsView(new V08_RamlApplication(model.getApiV08()));

        throw new ServiceUnavailableException("Unsupported RAML version.");
    }

    @Path("/")
    public static class Admin extends ApiDocsResource {
        public Admin(RamlModelResult model) {
            super(model);
        }
    }
}
