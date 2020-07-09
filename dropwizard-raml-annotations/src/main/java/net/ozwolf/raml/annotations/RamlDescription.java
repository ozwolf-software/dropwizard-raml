package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Description Annotation</h1>
 *
 * An annotation that can be used to add a description to:
 *
 * + resource methods
 * + resource method query and header parameters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlDescription {
    /**
     * @return the description
     */
    String value();
}
