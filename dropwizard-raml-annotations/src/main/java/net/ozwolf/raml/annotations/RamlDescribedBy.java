package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Described by Annotation</h1>
 *
 * An annotation used to provide a `describedBy` definition for security schemes and traits.
 *
 * @see RamlSecurityScheme
 * @see RamlTrait
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlDescribedBy {
    /**
     * The headers associated with the descriptor.
     *
     * @return the descriptor headers
     */
    RamlParameter[] headers() default {};

    /**
     * The query parameters associated with the descriptor.
     *
     * @return the descriptor query parameters
     */
    RamlParameter[] queryParameters() default {};

    /**
     * The responses associated with the descriptor
     *
     * @return the descriptor responses
     */
    RamlResponse[] responses() default {};
}
