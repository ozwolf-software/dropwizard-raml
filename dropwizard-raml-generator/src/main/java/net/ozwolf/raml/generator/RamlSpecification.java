package net.ozwolf.raml.generator;

import net.ozwolf.raml.generator.exception.RamlGenerationException;
import net.ozwolf.raml.generator.exception.RamlSpecificationException;
import org.apache.commons.lang3.StringUtils;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * <h1>RAML Specification</h1>
 *
 * A helper file for loading RAML specifications from multiple sources, logging errors and allowing later retrieval.
 */
public class RamlSpecification {
    private final Supplier<RamlModelResult> loader;
    private RamlModelResult model;

    private final static Logger LOGGER = LoggerFactory.getLogger("dropwizard-raml");

    /**
     * Generate a new RAML specification from the base package at runtime
     *
     * @param basePackage the base package to scan from for documentation generation.
     * @param version     the application version to be shown
     * @throws IllegalArgumentException if either argument is empty or null
     */
    public RamlSpecification(String basePackage, String version) {
        this.loader = () -> load(basePackage, version);
    }

    /**
     * Create a new RAML specification using a RAML file as it's source.
     *
     * @param ramlFile the RAML file to read
     * @throws IllegalArgumentException if the argument is empty or null
     */
    public RamlSpecification(String ramlFile) {
        this.loader = () -> load(ramlFile);
    }

    /**
     * Retrieve the RAML model result.  Will return an empty optional if the model is invalid.
     *
     * @return the RAML model result
     */
    public Optional<RamlModelResult> getModel() {
        if (model == null) {
            LOGGER.error("An error occurred while loading/generating RAML specifications.  See startup log for details.");
            return Optional.empty();
        } else {
            return Optional.of(model);
        }
    }

    /**
     * Initialize the model for use.
     */
    public synchronized void initialize() {
        if (this.model == null)
            this.model = this.loader.get();
    }

    private static RamlModelResult load(String ramlFile) {
        if (StringUtils.isBlank(ramlFile))
            throw new IllegalArgumentException("You must provide a RAML file to read.");

        RamlModelResult model = new RamlModelBuilder().buildApi(ramlFile);
        return verifyModel(model);
    }

    private static RamlModelResult load(String basePackage, String version) {
        if (StringUtils.isBlank(basePackage) || StringUtils.isBlank(version))
            throw new IllegalArgumentException("Both a base package to scan and an application version number needs to be provided.");

        try {
            RamlModelResult model = new RamlModelBuilder().buildApi(new RamlGenerator(basePackage, version).generate(), "/");
            return verifyModel(model);
        } catch (RamlGenerationException e) {
            LOGGER.error("Failed to generate RAML specification", e);
            return null;
        }
    }

    private static RamlModelResult verifyModel(RamlModelResult model) {
        if (model.hasErrors()) {
            LOGGER.error("RAML model contains errors.", new RamlSpecificationException(model.getValidationResults()));
            return null;
        } else {
            return model;
        }
    }
}
