package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML Schema Annotation</h1>
 *
 * An annotation that can be placed on a response or request class that either references a classpath schema file or a raw example value.
 *
 * If found on a class, it will override any attempt to auto-generate a schema document.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RamlSchema {
    /**
     * A schema value.
     *
     * Can either reference a classpath schema file or contain the raw schema document.
     *
     * The contents of this can either be in standard schema format for the content type or in a RAML-acceptable structure.
     *
     * @return the schema value
     */
    String value();
}
