package net.ozwolf.raml.apidocs.model;

import net.ozwolf.raml.apidocs.util.MarkDownHelper;

import java.util.List;

public interface RamlResource {
    String getDisplayName();

    String getDescription();

    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    String getPath();

    List<RamlParameter> getUriParameters();

    List<RamlMethod> getMethods();

    List<RamlResource> getResources();
}
