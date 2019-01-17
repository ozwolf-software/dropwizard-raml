package net.ozwolf.raml.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>RAML App Annotation</h1>
 *
 * This annotation is used to provide the overall description of an application.
 *
 * This is effectively the root document of the RAML specification.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RamlApp {
    /**
     * The title of the RAML application
     *
     * @return the application title
     */
    String title();

    /**
     * The description of the application's purpose.
     *
     * @return the application description
     */
    String description() default "";

    /**
     * The protocols the application can be accessed under (eg. HTTP, HTTPS, etc)
     *
     * @return the list of accessible protocols
     */
    String[] protocols() default {"HTTPS"};

    /**
     * The base URI of the application
     *
     * @return the application base URI
     */
    String baseUri();

    /**
     * References to application documentation.
     *
     * This documentation is used to provide a general overview of functionality to support the service.
     *
     * Documentation can be in the form of Markdown.
     *
     * @return the application documentation.
     */
    RamlDocumentation[] documentation() default {};

    /**
     * The security schemes the application supports.
     *
     * Can be used in conjunction with the {@literal @}RamlSecuredBy annotation on resource methods to document resource security requirements.
     *
     * @return the application's security schemes
     */
    RamlSecurityScheme[] security() default {};

    /**
     * The global traits of the application.  Can include descriptions of common request headers and query parameters as well as re-useable descriptors of responses.
     *
     * @return the application's traits
     */
    RamlTrait[] traits() default {};

    /**
     * Define global response definitions.  If a resource method does not explicitly define the status code of one of these responses, it will automatically be added.
     *
     * @return the default response definitions
     */
    RamlResponse[] globalResponses() default {};
}
