package net.ozwolf.raml.apidocs.model;

import net.ozwolf.raml.apidocs.util.MarkDownHelper;

public interface RamlDocumentation {
    String getTitle();

    String getContent();

    default String getContentHtml() {
        return MarkDownHelper.toHtml(getContent());
    }
}
