package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML URI Parameters</h1>
 *
 * An annotation used to describe the URI parameters associated with a resource class.
 *
 * Note: URI parameters associated with sub-resources within a resource class are defined via the `{@literal @}RamlSubResource` annotation.
 *
 * @see RamlSubResource
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RamlUriParameters {
    RamlParameter[] value();
}
