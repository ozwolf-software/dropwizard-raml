package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Example Annotation</h1>
 *
 * An annotation that can either be used to annotate a static method in a body type which will be used to generate an example payload or used with a resource method query or header parameter to generate an example value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface RamlExample {
    /**
     * An example value.
     *
     * This is ignored when the annotation is used in conjunction with an example static method.
     *
     * @return the example value
     */
    String value() default "";
}
