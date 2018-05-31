package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Sub-Resources Annotation</h1>
 *
 * An annotation to help describe the sub-resources contained within a resource class.
 *
 * As a single sub-resource path can be re-used for different methods, this annotation helps provide descriptive data for distinct paths.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RamlSubResources {
    RamlSubResource[] value();
}
