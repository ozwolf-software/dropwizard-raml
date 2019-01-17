package net.ozwolf.raml.apidocs;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import net.ozwolf.raml.apidocs.resources.ApiDocsResource;
import net.ozwolf.raml.common.AbstractRamlBundle;
import net.ozwolf.raml.generator.RamlSpecification;

/**
 * <h1>API Docs Bundle</h1>
 *
 * This DropWizard bundle is designed to add API specification features to your DropWizard service.
 *
 * This bundle will add the following resources under your service:
 *
 * + `/apidocs` - the human-readable of your RAML specification
 *
 * The RAML can either be read from a specific YAML file or generated from source by providing a package and a version number.
 */
public class ApiDocsBundle extends AbstractRamlBundle {
    /**
     * @inheritDocs
     */
    public ApiDocsBundle(RamlSpecification specification) {
        super(specification);
    }

    /**
     * @inheritDocs
     */
    public ApiDocsBundle(String basePackage, String version) {
        super(basePackage, version);
    }

    /**
     * @inheritDocs
     */
    public ApiDocsBundle(String ramlFile) {
        super(ramlFile);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new AssetsBundle("/apidocs-assets", "/apidocs/assets", null, "apidocs-view-assets"));
    }

    @Override
    protected void postInitialization(Environment environment) {
        environment.jersey().register(new ApiDocsResource(getSpecification()));
    }
}
