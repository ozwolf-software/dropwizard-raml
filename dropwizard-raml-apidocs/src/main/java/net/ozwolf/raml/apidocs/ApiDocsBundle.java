package net.ozwolf.raml.apidocs;

import io.dropwizard.Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import net.ozwolf.raml.apidocs.managed.ApiDocsManager;
import net.ozwolf.raml.apidocs.resources.ApiDocsResource;
import org.apache.commons.lang3.StringUtils;

/**
 * <h1>API Docs Bundle</h1>
 *
 * This DropWizard bundle is designed to add API specification features to your DropWizard service.
 *
 * This bundle will add the following resources under your service:
 *
 * + `/apidocs` - the machine-output of your RAML specification
 * + `GET application/json` - will return an easily parseable JSON format of the specification
 * + `GET text/yaml` - will return the full RAML specification
 *
 * The RAML can either be read from a specific YAML file or generated from source by providing a package and a version number.
 */
public class ApiDocsBundle implements Bundle {
    private String ramlFile;
    private String basePackage;
    private String version;

    private ApiDocsManager manager;

    /**
     * Create a new API docs bundle, using source code to generate the specification at runtime.
     *
     * @param basePackage the base package to scan from for documentation generation.
     * @param version     the application version to be shown
     * @throws IllegalArgumentException if either argument is empty or null
     */
    public ApiDocsBundle(String basePackage, String version) {
        if (StringUtils.isBlank(basePackage) || StringUtils.isBlank(version))
            throw new IllegalArgumentException("Both a base package to scan and an application version number needs to be provided.");

        this.basePackage = basePackage;
        this.version = version;
    }

    /**
     * Create a new API docs bundle that uses a specific RAML file as it's source.
     *
     * @param ramlFile the RAML file to read
     * @throws IllegalArgumentException if the argument is empty or null
     */
    public ApiDocsBundle(String ramlFile) {
        if (StringUtils.isBlank(ramlFile))
            throw new IllegalArgumentException("You must provide a RAML file to read.");
        this.ramlFile = ramlFile;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        this.manager = StringUtils.isBlank(ramlFile) ? ApiDocsManager.load(basePackage, version) : ApiDocsManager.load(ramlFile);

        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new AssetsBundle("/apidocs-assets", "/apidocs/assets", null, "apidocs-view-assets"));
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(new ApiDocsResource(manager));
    }


}
