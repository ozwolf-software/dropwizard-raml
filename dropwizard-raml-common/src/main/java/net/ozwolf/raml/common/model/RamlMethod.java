package net.ozwolf.raml.common.model;

import net.ozwolf.raml.common.util.MarkDownHelper;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

/**
 * <h1>RAML Method</h1>
 *
 * A method element for accessing the specified API
 */
public interface RamlMethod {
    /**
     * The method type (eg. GET)
     *
     * @return the method
     */
    String getMethod();

    /**
     * The description of this method in Markdown
     *
     * @return the description markdown
     */
    String getDescription();

    /**
     * The description of this method in HTML
     *
     * @return the description HTML
     */
    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    /**
     * The list of request headers this method will accept
     *
     * @return the request headers
     */
    List<RamlParameter> getHeaders();

    /**
     * The list of query parameters that can be used on this requested method
     *
     * @return the request query parameters
     */
    List<RamlParameter> getQueryParameters();

    /**
     * The request bodies that will be accepted by this method
     *
     * @return the request bodies
     */
    List<RamlBody> getRequests();

    /**
     * The responses this method will return
     *
     * @return the responses
     */
    List<RamlResponse> getResponses();

    /**
     * The security protecting this method
     *
     * @return the security
     */
    List<RamlSecurity> getSecurity();

    /**
     * Returns a request body definition for the provided content type
     *
     * @param mediaType the media type wanted
     * @return the body specification (if found)
     */
    default Optional<RamlBody> findRequestBodyFor(MediaType mediaType) {
        try {
            return getRequests()
                    .stream()
                    .filter(r -> {
                        MediaType bodyType = MediaType.valueOf(r.getContentType());
                        return bodyType.isCompatible(mediaType);
                    })
                    .findFirst();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns a response definition for the provided status code
     *
     * @param status the status code
     * @return the response specification (if found)
     */
    default Optional<RamlResponse> findResponseFor(Integer status) {
        for (RamlResponse response : getResponses())
            if (response.getStatus().equals(status))
                return Optional.of(response);

        return Optional.empty();
    }
}
