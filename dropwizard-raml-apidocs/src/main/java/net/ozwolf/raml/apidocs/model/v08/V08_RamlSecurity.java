package net.ozwolf.raml.apidocs.model.v08;

import net.ozwolf.raml.apidocs.model.RamlParameter;
import net.ozwolf.raml.apidocs.model.RamlResponse;
import net.ozwolf.raml.apidocs.model.RamlSecurity;
import org.raml.v2.api.model.v08.security.SecurityScheme;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class V08_RamlSecurity implements RamlSecurity {
    private final SecurityScheme scheme;

    public V08_RamlSecurity(SecurityScheme scheme) {
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
        return Optional.ofNullable(scheme.description()).map(StringType::value).orElse(null);
    }

    @Override
    public List<RamlParameter> getHeaders() {
        return scheme.describedBy().headers() == null ? newArrayList() : scheme.describedBy().headers().stream().map(V08_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlParameter> getQueryParameters() {
        return scheme.describedBy().queryParameters().stream().map(V08_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlResponse> getResponses() {
        return scheme.describedBy().responses().stream().map(V08_RamlResponse::new).collect(toList());
    }
}
