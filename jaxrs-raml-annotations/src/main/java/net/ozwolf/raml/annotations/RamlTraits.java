package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Traits Annotation</h1>
 *
 * An annotation to flag what traits a resource method is going to inherit.
 *
 * These traits are defined by the `{@literal @}RamlTrait` annotation.
 *
 * @see RamlTrait
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RamlTraits {
    String[] value();
}
