package net.ozwolf.raml.apidocs.model;

import net.ozwolf.raml.apidocs.util.MarkDownHelper;

import java.util.List;

public interface RamlMethod {
    String getMethod();

    String getDescription();

    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    List<RamlParameter> getHeaders();

    List<RamlParameter> getQueryParameters();

    List<RamlBody> getRequests();

    List<RamlResponse> getResponses();

    List<RamlSecurity> getSecurity();
}
