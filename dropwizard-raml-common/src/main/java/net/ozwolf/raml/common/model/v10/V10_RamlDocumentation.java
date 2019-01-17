package net.ozwolf.raml.common.model.v10;

import net.ozwolf.raml.common.model.RamlDocumentation;
import org.raml.v2.api.model.v10.api.DocumentationItem;

/**
 * <h1>RAML 1.0 Documentation</h1>
 *
 * A 1.0 definition of a documentation item
 */
public class V10_RamlDocumentation implements RamlDocumentation {
    private final DocumentationItem documentation;

    /**
     * Create a new 1.0 documentation item
     *
     * @param documentation the documentation
     */
    public V10_RamlDocumentation(DocumentationItem documentation) {
        this.documentation = documentation;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getTitle() {
        return documentation.title().value();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getContent() {
        return documentation.content().value();
    }
}
