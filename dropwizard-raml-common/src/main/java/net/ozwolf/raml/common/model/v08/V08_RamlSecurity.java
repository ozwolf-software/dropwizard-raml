package net.ozwolf.raml.common.model.v08;

import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlResponse;
import net.ozwolf.raml.common.model.RamlSecurity;
import org.raml.v2.api.model.v08.security.SecurityScheme;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 0.8 Security</h1>
 *
 * A 0.8 representation of an API security scheme
 */
public class V08_RamlSecurity implements RamlSecurity {
    private final SecurityScheme scheme;

    /**
     * Create a new 0.8 API security scheme
     *
     * @param scheme the security scheme
     */
    public V08_RamlSecurity(SecurityScheme scheme) {
        this.scheme = scheme;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getName() {
        return scheme.name();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getType() {
        return scheme.type();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(scheme.description()).map(StringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getHeaders() {
        return scheme.describedBy().headers() == null ? newArrayList() : scheme.describedBy().headers().stream().map(V08_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getQueryParameters() {
        return scheme.describedBy().queryParameters().stream().map(V08_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlResponse> getResponses() {
        return scheme.describedBy().responses().stream().map(V08_RamlResponse::new).collect(toList());
    }
}
