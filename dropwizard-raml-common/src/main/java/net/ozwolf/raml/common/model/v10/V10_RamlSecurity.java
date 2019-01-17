package net.ozwolf.raml.common.model.v10;

import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlResponse;
import net.ozwolf.raml.common.model.RamlSecurity;
import org.raml.v2.api.model.v10.security.SecurityScheme;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 1.0 Security</h1>
 *
 * A 1.0 definition of a security scheme
 */
public class V10_RamlSecurity implements RamlSecurity {
    private final SecurityScheme scheme;

    /**
     * Create a new 1.0 security scheme item
     *
     * @param scheme the security scheme
     */
    public V10_RamlSecurity(SecurityScheme scheme) {
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
        return Optional.ofNullable(scheme.description()).map(AnnotableStringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getHeaders() {
        return scheme.describedBy().headers() == null ? newArrayList() : scheme.describedBy().headers().stream().map(V10_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getQueryParameters() {
        return scheme.describedBy().queryParameters().stream().map(V10_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlResponse> getResponses() {
        return scheme.describedBy().responses().stream().map(V10_RamlResponse::new).collect(toList());
    }
}
