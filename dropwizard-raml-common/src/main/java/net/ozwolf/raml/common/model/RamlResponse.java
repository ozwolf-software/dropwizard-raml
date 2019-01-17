package net.ozwolf.raml.common.model;

import net.ozwolf.raml.common.util.MarkDownHelper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * <h1>RAML Response</h1>
 *
 * A specification for a method response
 */
public interface RamlResponse {
    /**
     * The response status code
     *
     * @return the stauts code
     */
    Integer getStatus();

    /**
     * An English representation of the status code
     *
     * @return the status name
     */
    default String getStatusName() {
        Response.Status status = Response.Status.fromStatusCode(getStatus());

        if (status != null)
            return status.getReasonPhrase();

        switch (getStatus()) {
            case 422:
                return "Unprocessible Entity";
            case 423:
                return "Locked";
            default:
                return "";
        }
    }

    /**
     * The description of what the response represents
     *
     * @return the description markdown
     */
    String getDescription();

    /**
     * The description of what the response represents
     *
     * @return the description HTML
     */
    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    /**
     * The headers that are or can be returned with the response
     *
     * @return the response headers
     */
    List<RamlParameter> getHeaders();

    /**
     * The response content type bodies that can be returned
     *
     * @return the response bodies
     */
    List<RamlBody> getBodies();

    /**
     * Returns a response body definition for the provided content type
     *
     * @param contentType the content type wanted
     * @return the body specification (if found)
     */
    default Optional<RamlBody> findBodyFor(String contentType) {
        try {
            return Optional.ofNullable(MediaType.valueOf(contentType))
                    .flatMap(this::findBodyFor);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    default Optional<RamlBody> findBodyFor(MediaType mediaType) {
        try {
            return getBodies()
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
}