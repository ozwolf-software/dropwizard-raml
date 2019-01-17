package net.ozwolf.raml.common.model.v08;

import net.ozwolf.raml.common.model.RamlDocumentation;
import org.raml.v2.api.model.v08.api.DocumentationItem;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.Optional;

/**
 * <h1>RAML 0.8 Documentation</h1>
 *
 * A 0.8 definition of a documentation item
 */
public class V08_RamlDocumentation implements RamlDocumentation {
    private final DocumentationItem documentation;

    /**
     * Create a new 0.8 documentation item
     *
     * @param documentation the documentation
     */
    public V08_RamlDocumentation(DocumentationItem documentation) {
        this.documentation = documentation;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getTitle() {
        return documentation.title();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getContent() {
        return Optional.ofNullable(documentation.content()).map(StringType::value).orElse("No Content");
    }
}
