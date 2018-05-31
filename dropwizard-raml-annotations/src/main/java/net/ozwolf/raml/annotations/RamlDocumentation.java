package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Documentation Annotation</h1>
 *
 * An annotation used to describe a documentation resource.
 *
 * @see RamlApp
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlDocumentation {
    /**
     * The title of the documentation page.
     *
     * @return the title
     */
    String title();

    /**
     * The content of the documentation.  Can reference a classpath resource or can be a direct text entry.
     *
     * Supports markdown.
     *
     * @return the content
     */
    String content();
}
