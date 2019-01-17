package net.ozwolf.raml.common;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.generator.RamlSpecification;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

/**
 * <h1>RAML Bundles</h1>
 *
 * A DropWizard bundle that allows multiple RAML bundles to be registered, ensuring specifications only get derived once then shared amongst other bundles.
 */
public class RamlBundles extends AbstractRamlBundle {
    private final List<Class<? extends Bundle>> links;

    /**
     * Create a new RAML bundle using the provided specification
     *
     * @param specification the RAML specification
     */
    public RamlBundles(RamlSpecification specification) {
        super(specification);
        this.links = newArrayList();
    }

    /**
     * Create a new RAML bundle using source code to generate the specification at runtime.
     *
     * @param basePackage the base package to scan from for documentation generation.
     * @param version     the application version to be shown
     * @throws IllegalArgumentException if either argument is empty or null
     */
    public RamlBundles(String basePackage, String version) {
        super(basePackage, version);
        this.links = newArrayList();
    }

    /**
     * Create a new RAML bundle a specific RAML file as it's source.
     *
     * @param ramlFile the RAML file to read
     * @throws IllegalArgumentException if the argument is empty or null
     */
    public RamlBundles(String ramlFile) {
        super(ramlFile);
        this.links = newArrayList();
    }

    /**
     * Link a bundle to this collection.  The bundle _must_ have a constructor that accepts a `RamlSpecification` instance.
     *
     * @param bundle the bundle to link
     * @return the updated bundle
     */
    public RamlBundles link(Class<? extends Bundle> bundle) {
        if (getConstructorFor(bundle) == null)
            throw new IllegalArgumentException("Bundle must have a constructor that accepts a [ " + RamlSpecification.class.getSimpleName() + " ] instance.");

        this.links.add(bundle);
        return this;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        for (Class<? extends Bundle> bundle : links)
            Optional.ofNullable(instantiate(bundle)).ifPresent(bootstrap::addBundle);
    }

    @Override
    protected void postInitialization(Environment environment) {

    }

    private Constructor<? extends Bundle> getConstructorFor(Class<? extends Bundle> bundle) {
        try {
            return bundle.getConstructor(RamlSpecification.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Bundle instantiate(Class<? extends Bundle> bundle) {
        try {
            Constructor<? extends Bundle> constructor = getConstructorFor(bundle);
            if (constructor == null)
                return null;

            return constructor.newInstance(getSpecification());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not instantiate [ " + bundle.getSimpleName() + " ].", e);
        }
    }
}
