package net.ozwolf.raml.common.model;

import net.ozwolf.raml.common.util.MarkDownHelper;

import java.util.List;
import java.util.Optional;

/**
 * <h1>RAML Resource</h1>
 *
 * An API resource specification
 */
public interface RamlResource {
    /**
     * The resource display name
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     * The description of what the resource does
     *
     * @return the description markdown
     */
    String getDescription();

    /**
     * The description of what the resource does
     *
     * @return the description HTML
     */
    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    /**
     * The resource path
     *
     * @return the path
     */
    String getPath();

    /**
     * The URI parameters of the resource
     *
     * @return the URI parameters
     */
    List<RamlParameter> getUriParameters();

    /**
     * The methods for this resource
     *
     * @return the methods
     */
    List<RamlMethod> getMethods();

    /**
     * The sub-resources for this resource
     *
     * @return the sub-resources
     */
    List<RamlResource> getResources();

    /**
     * Return a method definition on this resource
     *
     * @param method the method type
     * @return the method if found
     */
    default Optional<RamlMethod> find(String method) {
        return getMethods().stream()
                .filter(m -> m.getMethod().equalsIgnoreCase(method))
                .findFirst();
    }
}
