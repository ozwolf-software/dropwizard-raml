package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Security Annotation</h1>
 *
 * Used to describe a security scheme.
 *
 * Works in conjunction with the `{@literal @}RamlApp` annotation.
 *
 * Types must adhere to the [allowed security schemes](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md#security-scheme-types) defined by the RAML 1.0 specification.
 *
 * @see RamlApp
 * @see RamlSecuredBy
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlSecurity {
    /**
     * The security scheme key.
     *
     * This key is use with the `{@literal @}RamlSecuredBy` annotation to link resource methods to this security scheme
     *
     * @return the security scheme key
     */
    String key();

    /**
     * The type of security scheme.
     *
     * Must adhere to the allowed types defined by the RAML specification.
     *
     * @return the security scheme type
     */
    String type();

    /**
     * The description of the security scheme.
     *
     * @return the security scheme description
     */
    String description() default "";

    /**
     * The overall descriptor of the security scheme, covering required headers, query parameters and associated responses.
     *
     * @return the security scheme descriptor
     */
    RamlDescriptor describedBy();
}
