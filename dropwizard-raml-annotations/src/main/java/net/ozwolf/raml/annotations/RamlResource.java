package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Resource Annotation</h1>
 *
 * Annotation used to describe a top-level resource class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RamlResource {
    /**
     * The display name for the resource
     *
     * @return the resource display name
     */
    String displayName();

    /**
     * The description of the resource's purpose
     *
     * @return the resource description
     */
    String description();

    /**
     * Define a display order for your documentation.
     *
     * If resources are found with the same display order, they will be sorted alphabetically in the API docs view.
     *
     * @return the resource display order
     */
    int displayOrder() default 999999;
}
