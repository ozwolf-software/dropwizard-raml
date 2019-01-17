package net.ozwolf.raml.common.model;

import net.ozwolf.raml.common.util.MarkDownHelper;

import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <h1>RAML Application</h1>
 *
 * A representation of a RAML application from a specification
 */
public interface RamlApplication {
    /**
     * The application title
     *
     * @return the title
     */
    String getTitle();

    /**
     * The application version
     *
     * @return the version
     */
    String getVersion();

    /**
     * The application description in Markdown
     *
     * @return the description markdown
     */
    String getDescription();

    /**
     * The application description in HTML
     *
     * @return the description HTML
     */
    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    /**
     * The application access protocols
     *
     * @return the protocols
     */
    Set<String> getProtocols();

    /**
     * The application base URI
     *
     * @return the base URI
     */
    String getBaseUri();

    /**
     * The documentation elements
     *
     * @return the app documentation
     */
    List<RamlDocumentation> getDocumentation();

    /**
     * The API resource specifications
     *
     * @return the app resources
     */
    List<RamlResource> getResources();

    /**
     * Retrieve a RAML method based on the resource path and the method type.
     *
     * Will return an empty optional if not found.
     *
     * @param resourcePath    the resource path
     * @param subResourcePath the sub-resource path
     * @return the API method
     */
    default Optional<RamlResource> find(String resourcePath, String subResourcePath) {
        return getResources().stream()
                .filter(r -> r.getPath().equalsIgnoreCase(resourcePath))
                .findFirst()
                .flatMap(r -> {
                    if (subResourcePath == null)
                        return Optional.of(r);

                    UriBuilder builder = UriBuilder.fromPath(resourcePath).path(subResourcePath);

                    return r.getResources()
                            .stream()
                            .filter(sr -> sr.getPath().equalsIgnoreCase(builder.toTemplate()))
                            .findFirst();
                });
    }
}
