package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlBody;
import net.ozwolf.raml.apidocs.model.RamlParameter;
import net.ozwolf.raml.apidocs.model.RamlResponse;
import org.raml.v2.api.model.v10.bodies.Response;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class V10_RamlResponse implements RamlResponse {
    private final Response response;

    public V10_RamlResponse(Response response) {
        this.response = response;
    }

    @Override
    public Integer getStatus() {
        return Integer.valueOf(response.code().value());
    }

    @Override
    public String getDescription() {
        return response.description().value();
    }

    @Override
    public List<RamlParameter> getHeaders() {
        return response.headers() == null ? newArrayList() : response.headers().stream().map(V10_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlBody> getBodies() {
        return response.body() == null ? newArrayList() : response.body().stream().map(V10_RamlBody::new).collect(toList());
    }
}
