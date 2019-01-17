package net.ozwolf.raml.common.model.v10;

import net.ozwolf.raml.common.model.*;
import org.raml.v2.api.model.v10.methods.Method;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 1.0 Method</h1>
 *
 * A 1.0 definition of an API method
 */
public class V10_RamlMethod implements RamlMethod {
    private final Method method;

    /**
     * Create a new 1.0 method item
     *
     * @param method the method
     */
    public V10_RamlMethod(Method method) {
        this.method = method;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMethod() {
        return method.method();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(method.description()).map(AnnotableStringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlSecurity> getSecurity() {
        return method.securedBy() == null ? newArrayList() : method.securedBy().stream().map(s -> new V10_RamlSecurity(s.securityScheme())).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getHeaders() {
        return method.headers() == null ? newArrayList() : method.headers().stream().map(V10_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getQueryParameters() {
        return method.queryParameters() == null ? newArrayList() : method.queryParameters().stream().map(V10_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlBody> getRequests() {
        return method.body() == null ? newArrayList() : method.body().stream().map(V10_RamlBody::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlResponse> getResponses() {
        Map<Integer, RamlResponse> responses = newHashMap();
        method.responses().stream().map(V10_RamlResponse::new).forEach(r -> responses.put(r.getStatus(), r));
        getSecurity().stream().flatMap(s -> s.getResponses().stream()).forEach(r -> {
            if (!responses.containsKey(r.getStatus()))
                responses.put(r.getStatus(), r);
        });

        return responses.values().stream().sorted(Comparator.comparing(RamlResponse::getStatus)).collect(toList());
    }
}
