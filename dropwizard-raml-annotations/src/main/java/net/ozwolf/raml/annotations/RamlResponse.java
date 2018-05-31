package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Response Annotation</h1>
 *
 * An annotation used to describe a response in greater detail.
 *
 * The generator can generate response information based purely on the signature of methods.  However, this will assume `200` response codes and will miss any other response codes that may be returned.
 *
 * Used in conjunction with the `{@literal @}RamlResponses` annotation.
 *
 * @see RamlResponse
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlResponse {
    /**
     * The status code of the response
     *
     * @return the status code
     */
    int status();

    /**
     * The description of what the response represents
     *
     * @return the description
     */
    String description();

    /**
     * The headers that would be included in the response.
     *
     * @return the response headers
     */
    RamlParameter[] headers() default {};

    /**
     * The body payloads that would be returned based on the use of the `Accept` request header.
     *
     * @return the response bodies
     */
    RamlBody[] bodies() default {};
}
