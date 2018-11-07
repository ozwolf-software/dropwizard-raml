package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlDocumentation;
import org.raml.v2.api.model.v10.api.DocumentationItem;

public class V10_RamlDocumentation implements RamlDocumentation {
    private final DocumentationItem documentation;

    public V10_RamlDocumentation(DocumentationItem documentation) {
        this.documentation = documentation;
    }

    @Override
    public String getTitle() {
        return documentation.title().value();
    }

    @Override
    public String getContent() {
        return documentation.content().value();
    }
}
