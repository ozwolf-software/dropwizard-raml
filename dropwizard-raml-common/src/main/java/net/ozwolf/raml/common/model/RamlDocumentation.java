package net.ozwolf.raml.common.model;

import net.ozwolf.raml.common.util.MarkDownHelper;

/**
 * <h1>RAML Documentation</h1>
 *
 * A documentation element for the application
 */
public interface RamlDocumentation {
    /**
     * The documentation title
     *
     * @return the title
     */
    String getTitle();

    /**
     * The documentation content in Markdown
     *
     * @return the content
     */
    String getContent();

    /**
     * The documentation content in HTML
     *
     * @return the content HTML
     */
    default String getContentHtml() {
        return MarkDownHelper.toHtml(getContent());
    }
}
