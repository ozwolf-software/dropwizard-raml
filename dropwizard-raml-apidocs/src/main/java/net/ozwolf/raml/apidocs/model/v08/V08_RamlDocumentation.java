package net.ozwolf.raml.apidocs.model.v08;

import net.ozwolf.raml.apidocs.model.RamlDocumentation;
import org.raml.v2.api.model.v08.api.DocumentationItem;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.Optional;

public class V08_RamlDocumentation implements RamlDocumentation {
    private final DocumentationItem documentation;

    public V08_RamlDocumentation(DocumentationItem documentation) {
        this.documentation = documentation;
    }

    @Override
    public String getTitle() {
        return documentation.title();
    }

    @Override
    public String getContent() {
        return Optional.ofNullable(documentation.content()).map(StringType::value).orElse("No Content");
    }
}
