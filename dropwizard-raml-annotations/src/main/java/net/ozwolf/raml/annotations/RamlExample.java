package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Example Annotation</h1>
 *
 * This annotation can be used to:
 *
 * <ul>
 *     <li>1. Used to annotate a request or response class, with the value either referencing a classpath file or containing specific contents.</li>
 *     <li>2. Annotate a static method on a request or response class that generates an example instance of that class.</li>
 *     <li>3. Used to annotate a header or query parameter in a method to provide an example.</li>
 * </ul>
 *
 * This annotation on a type will trump the annotation on the static method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface RamlExample {
    /**
     * An example value.  Can either be a raw value or reference a classpath example file (for request/response classes).
     *
     * This is ignored when the annotation is used in conjunction with an example static method.
     *
     * @return the example value
     */
    String value() default "";
}
