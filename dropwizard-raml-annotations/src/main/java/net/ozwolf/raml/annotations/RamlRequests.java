package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Requests Annotation</h1>
 *
 * Used to describe the allowed request bodies for a `POST`, `PUT` or `PATCH` resource method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RamlRequests {
    RamlBody[] value();
}
