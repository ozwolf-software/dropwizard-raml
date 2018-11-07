package net.ozwolf.raml.apidocs.model.v08;

import net.ozwolf.raml.apidocs.model.*;
import org.raml.v2.api.model.v08.methods.Method;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class V08_RamlMethod implements RamlMethod {
    private final Method method;

    public V08_RamlMethod(Method method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method.method();
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(method.description()).map(StringType::value).orElse(null);
    }

    @Override
    public List<RamlSecurity> getSecurity() {
        return method.securedBy() == null ? newArrayList() : method.securedBy().stream().map(s -> new V08_RamlSecurity(s.securityScheme())).collect(toList());
    }

    @Override
    public List<RamlParameter> getHeaders() {
        return method.headers() == null ? newArrayList() : method.headers().stream().map(V08_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlParameter> getQueryParameters() {
        return method.queryParameters() == null ? newArrayList() : method.queryParameters().stream().map(V08_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlBody> getRequests() {
        return method.body() == null ? newArrayList() : method.body().stream().map(V08_RamlBody::new).collect(toList());
    }

    @Override
    public List<RamlResponse> getResponses() {
        return method.responses().stream().map(V08_RamlResponse::new).sorted(Comparator.comparing(V08_RamlResponse::getStatus)).collect(toList());
    }
}
