package net.ozwolf.raml.apidocs.model.v08;

import net.ozwolf.raml.apidocs.model.RamlBody;
import net.ozwolf.raml.apidocs.model.RamlParameter;
import net.ozwolf.raml.apidocs.model.RamlResponse;
import org.raml.v2.api.model.v08.bodies.Response;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class V08_RamlResponse implements RamlResponse {
    private final Response response;

    public V08_RamlResponse(Response response) {
        this.response = response;
    }

    @Override
    public Integer getStatus() {
        return Integer.valueOf(response.code().value());
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(response.description()).map(StringType::value).orElse(null);
    }

    @Override
    public List<RamlParameter> getHeaders() {
        return response.headers() == null ? newArrayList() : response.headers().stream().map(V08_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlBody> getBodies() {
        return response.body() == null ? newArrayList() : response.body().stream().map(V08_RamlBody::new).collect(toList());
    }
}
