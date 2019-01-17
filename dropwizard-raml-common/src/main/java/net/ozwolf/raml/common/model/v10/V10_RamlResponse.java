package net.ozwolf.raml.common.model.v10;

import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlResponse;
import org.raml.v2.api.model.v10.bodies.Response;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 1.0 Response</h1>
 *
 * A 1.0 definition of a response
 */
public class V10_RamlResponse implements RamlResponse {
    private final Response response;

    /**
     * Create a new 1.0 response item
     * @param response the response
     */
    public V10_RamlResponse(Response response) {
        this.response = response;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Integer getStatus() {
        return Integer.valueOf(response.code().value());
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(response.description()).map(AnnotableStringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getHeaders() {
        return response.headers() == null ? newArrayList() : response.headers().stream().map(V10_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlBody> getBodies() {
        return response.body() == null ? newArrayList() : response.body().stream().map(V10_RamlBody::new).collect(toList());
    }
}
