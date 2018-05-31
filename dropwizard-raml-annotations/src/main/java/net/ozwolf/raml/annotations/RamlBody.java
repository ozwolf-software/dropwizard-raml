package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Body Annotation</h1>
 *
 * This annotation is used to help describe a RAML body type for both requests and responses.
 *
 * @see RamlResponse
 * @see RamlRequests
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlBody {
    /**
     * The content type of the body(eg. "application/json")
     *
     * @return the body content type
     */
    String contentType();

    /**
     * A reference to the object type used for this body.
     *
     * If this is supplied, the generator will attempt to use an appropriate generator to generate a schema document for the object.
     *
     * If the object has a {@literal @}RamlExample annotated static method that returns a concrete example of the type, the generator will use that to create an example payload.
     *
     * If not value is provided here, the generator will resort to the contents of the `schema()` and `example()` properties to derive body schema and example
     *
     * @return the body object type
     */
    Class<?> type() default NotDefinedReturnType.class;

    /**
     * The schema for this body type.  The value in here can make reference to a classpath resource _or_ a direct text entry.
     *
     * This will be used to supply a schema in the RAML specification _if_ the `type()` property has not be defined.
     *
     * @return the body schema
     */
    String schema() default "";

    /**
     * The example for this body type.  The value in here can make reference to a classpath resource _or_ a direct text entry.
     *
     * This will be used to supply a schema in the RAML specification _if_ the `type()` property has not been defined.
     *
     * @return the body example
     */
    String example() default "";

    final class NotDefinedReturnType {
    }
}
