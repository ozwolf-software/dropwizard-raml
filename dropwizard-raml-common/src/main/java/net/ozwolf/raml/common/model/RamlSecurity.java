package net.ozwolf.raml.common.model;

import net.ozwolf.raml.common.util.MarkDownHelper;

import java.util.List;

/**
 * <h1>RAML Security</h1>
 *
 * A representation of the a security scheme
 */
public interface RamlSecurity {
    /**
     * The security scheme name
     *
     * @return the name
     */
    String getName();

    /**
     * The security scheme type
     *
     * @return the type
     */
    String getType();

    /**
     * The description of what the security represents
     *
     * @return the description markdown
     */
    String getDescription();

    /**
     * The description of what the security represents
     *
     * @return the description HTML
     */
    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    /**
     * The headers used by the security scheme
     *
     * @return the security headers
     */
    List<RamlParameter> getHeaders();

    /**
     * The query parameters used by the security scheme
     *
     * @return the security query parameters
     */
    List<RamlParameter> getQueryParameters();

    /**
     * The list of potential responses returned by the security scheme
     *
     * @return the security responses
     */
    List<RamlResponse> getResponses();
}
