package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML SecuredBy Annotation</h1>
 *
 * An annotation to flag what security schemes a resource method is protected by.
 *
 * These security schemes are defined by the `{@literal @}RamlSecurity` annotation.
 *
 * @see RamlSecurity
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RamlSecuredBy {
    String[] value();
}
