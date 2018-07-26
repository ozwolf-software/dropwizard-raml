package net.ozwolf.raml.apidocs;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.apidocs.exception.RamlSpecificationException;
import net.ozwolf.raml.apidocs.resources.ApiDocsResource;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;

public class ApiDocsBundle implements Bundle {
    private final String ramlFile;

    private RamlModelResult model;

    public ApiDocsBundle(String ramlFile) {
        this.ramlFile = ramlFile;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        this.model = new RamlModelBuilder().buildApi(ramlFile);
        if (this.model.hasErrors())
            throw new RamlSpecificationException(this.model.getValidationResults());
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(new ApiDocsResource(model));
    }
}
