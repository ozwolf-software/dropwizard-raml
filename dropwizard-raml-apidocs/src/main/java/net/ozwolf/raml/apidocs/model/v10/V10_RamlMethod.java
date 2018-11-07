package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.*;
import org.raml.v2.api.model.v10.methods.Method;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class V10_RamlMethod implements RamlMethod {
    private final Method method;

    public V10_RamlMethod(Method method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method.method();
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(method.description()).map(AnnotableStringType::value).orElse(null);
    }

    @Override
    public List<RamlSecurity> getSecurity() {
        return method.securedBy() == null ? newArrayList() : method.securedBy().stream().map(s -> new V10_RamlSecurity(s.securityScheme())).collect(toList());
    }

    @Override
    public List<RamlParameter> getHeaders() {
        return method.headers() == null ? newArrayList() : method.headers().stream().map(V10_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlParameter> getQueryParameters() {
        return method.queryParameters() == null ? newArrayList() : method.queryParameters().stream().map(V10_RamlParameter::new).collect(toList());
    }

    @Override
    public List<RamlBody> getRequests() {
        return method.body() == null ? newArrayList() : method.body().stream().map(V10_RamlBody::new).collect(toList());
    }

    @Override
    public List<RamlResponse> getResponses() {
        return method.responses().stream().map(V10_RamlResponse::new).sorted(Comparator.comparing(V10_RamlResponse::getStatus)).collect(toList());
    }
}
