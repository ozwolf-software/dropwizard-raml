package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Query Parameters Annotation</h1>
 *
 * An annotation that can be used in conjunction with a resource method to describe request query parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RamlQueryParameters {
    RamlParameter[] value();
}
