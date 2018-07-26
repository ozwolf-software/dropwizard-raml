package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlParameter;
import net.ozwolf.raml.apidocs.model.RamlResponse;
import net.ozwolf.raml.apidocs.model.RamlSecurity;
import org.raml.v2.api.model.v10.security.SecurityScheme;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class V10_RamlSecurity implements RamlSecurity {
    private final SecurityScheme scheme;

    public V10_RamlSecurity(SecurityScheme scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getName() {
        return scheme.name();
    }

    @Override
    public String getType() {
        return scheme.type();
    }

    @Override
    public String getDescription() {
        return scheme.description().value();
    }

    @Override
    public List<RamlParameter> getHeaders() {
        return scheme.describedBy().headers() == null ? newArrayList() : scheme.describedBy().headers().stream().map(V10_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlParameter> getQueryParameters() {
        return scheme.describedBy().queryParameters().stream().map(V10_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlResponse> getResponses() {
        return scheme.describedBy().responses().stream().map(V10_RamlResponse::new).collect(toList());
    }
}
