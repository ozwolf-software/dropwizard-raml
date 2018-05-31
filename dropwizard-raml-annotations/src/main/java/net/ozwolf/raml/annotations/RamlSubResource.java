package net.ozwolf.raml.annotations;

import javax.ws.rs.Path;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Sub-Resource Annotation</h1>
 *
 * Used in conjunction with the `{@literal @}RamlSubResources` annotation to describe sub-resource paths within a resource class.
 *
 * If the sub-resource contains URI parameters, one of these annotations needs to be supplied to describe the URI parameters correctly.
 *
 * @see RamlSubResources
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlSubResource {
    /**
     * The path the sub-resource maps to.
     *
     * This path value should simply match the `{@literal @}Path annotation on the associated method(s).
     *
     * @return the sub-resource path.
     */
    Path path();

    /**
     * The description of the sub-resources purpose.
     *
     * @return the sub-resource description
     */
    String description() default "";

    /**
     * The URI parameters associated with the sub-resource path.
     *
     * @return the sub-resource URI parameters
     */
    RamlParameter[] uriParameters() default {};
}
