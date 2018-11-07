package net.ozwolf.raml.apidocs.model;

import net.ozwolf.raml.apidocs.util.MarkDownHelper;

import java.util.List;
import java.util.Set;

public interface RamlApplication {
    String getTitle();

    String getVersion();

    String getDescription();

    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    Set<String> getProtocols();

    String getBaseUri();

    List<RamlDocumentation> getDocumentation();

    List<RamlResource> getResources();
}
