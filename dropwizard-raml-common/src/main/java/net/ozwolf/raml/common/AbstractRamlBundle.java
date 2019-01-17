package net.ozwolf.raml.common;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.generator.RamlSpecification;

public abstract class AbstractRamlBundle implements Bundle {
    private final RamlSpecification specification;

    /**
     * Create a new bundle from the provided specification
     *
     * @param specification the RAML specification
     */
    protected AbstractRamlBundle(RamlSpecification specification) {
        this.specification = specification;
    }

    /**
     * Create a new bundle, using source code to generate the specification at runtime.
     *
     * @param basePackage the base package to scan from for documentation generation.
     * @param version     the application version to be shown
     * @throws IllegalArgumentException if either argument is empty or null
     */
    protected AbstractRamlBundle(String basePackage, String version) {
        this.specification = new RamlSpecification(basePackage, version);
    }

    /**
     * Create a new bundle that uses a specific RAML file as it's source.
     *
     * @param ramlFile the RAML file to read
     * @throws IllegalArgumentException if the argument is empty or null
     */
    protected AbstractRamlBundle(String ramlFile) {
        this.specification = new RamlSpecification(ramlFile);
    }

    /**
     * Retrieve the RAML specification.
     *
     * @return the specification
     */
    protected RamlSpecification getSpecification() {
        return specification;
    }

    @Override
    public void run(Environment environment) {
        specification.initialize();
        postInitialization(environment);
    }

    protected abstract void postInitialization(Environment environment);
}
