package net.ozwolf.raml.apidocs.resources;

import net.ozwolf.raml.apidocs.model.RamlApplication;
import net.ozwolf.raml.apidocs.model.v10.V10_RamlApplication;
import org.raml.v2.api.RamlModelResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;

@Path("/apidocs")
public class ApiDocsResource {
    private final RamlModelResult model;

    public ApiDocsResource(RamlModelResult model) {
        this.model = model;
    }

    @GET
    @Produces("application/json")
    public RamlApplication getApplication() {
        if (model.isVersion10())
            return new V10_RamlApplication(model.getApiV10());

        throw new ServiceUnavailableException("RAML 0.8 currently not supported.");
    }

}
