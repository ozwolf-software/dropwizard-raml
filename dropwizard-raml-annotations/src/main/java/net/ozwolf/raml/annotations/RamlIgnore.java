package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Ignore Annotation</h1>
 *
 * This annotation is used on resources and methods to tell the generator to ignore the entity.
 *
 * Useful when components are technically unavailable.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlIgnore {
}
