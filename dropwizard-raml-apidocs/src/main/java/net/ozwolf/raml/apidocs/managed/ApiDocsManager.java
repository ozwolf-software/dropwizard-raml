package net.ozwolf.raml.apidocs.managed;

import net.ozwolf.raml.apidocs.exception.RamlSpecificationException;
import net.ozwolf.raml.generator.RamlGenerator;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiDocsManager {
    private final RamlModelResult model;
    private final RamlGenerationException exception;

    private final static Logger LOGGER = LoggerFactory.getLogger(ApiDocsManager.class);

    private ApiDocsManager(RamlModelResult model) {
        this.model = model;
        this.exception = null;
    }

    private ApiDocsManager(RamlGenerationException exception) {
        this.exception = exception;
        this.model = null;
    }

    public RamlModelResult getModel() {
        if (this.exception != null) {
            LOGGER.error("Failed to load RAML specification.", this.exception);
            return null;
        }

        if (model != null && model.hasErrors()) {
            LOGGER.error("RAML model contains errors.", new RamlSpecificationException(model.getValidationResults()));
            return null;
        }

        return model;
    }

    public static ApiDocsManager load(String ramlFile) {
        return new ApiDocsManager(new RamlModelBuilder().buildApi(ramlFile));
    }

    public static ApiDocsManager load(String basePackage, String version) {
        try {
            RamlModelResult result = new RamlModelBuilder().buildApi(new RamlGenerator(basePackage, version).generate(), "/");
            return new ApiDocsManager(result);
        } catch (RamlGenerationException e) {
            return new ApiDocsManager(e);
        }
    }
}
