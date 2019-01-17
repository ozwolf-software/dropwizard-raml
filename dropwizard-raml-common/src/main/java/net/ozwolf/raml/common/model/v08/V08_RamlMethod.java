package net.ozwolf.raml.common.model.v08;

import net.ozwolf.raml.common.model.*;
import org.raml.v2.api.model.v08.methods.Method;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 0.8 Method</h1>
 *
 * A 0.8 definition of an API method
 */
public class V08_RamlMethod implements RamlMethod {
    private final Method method;

    /**
     * Create a new 0.8 API method item
     *
     * @param method the method
     */
    public V08_RamlMethod(Method method) {
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
        return Optional.ofNullable(method.description()).map(StringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlSecurity> getSecurity() {
        return method.securedBy() == null ? newArrayList() : method.securedBy().stream().map(s -> new V08_RamlSecurity(s.securityScheme())).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getHeaders() {
        return method.headers() == null ? newArrayList() : method.headers().stream().map(V08_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getQueryParameters() {
        return method.queryParameters() == null ? newArrayList() : method.queryParameters().stream().map(V08_RamlParameter::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlBody> getRequests() {
        return method.body() == null ? newArrayList() : method.body().stream().map(V08_RamlBody::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlResponse> getResponses() {
        Map<Integer, RamlResponse> responses = newHashMap();
        method.responses().stream().map(V08_RamlResponse::new).forEach(r -> responses.put(r.getStatus(), r));
        getSecurity().stream().flatMap(s -> s.getResponses().stream()).forEach(r -> {
            if (!responses.containsKey(r.getStatus()))
                responses.put(r.getStatus(), r);
        });

        return responses.values().stream().sorted(Comparator.comparing(RamlResponse::getStatus)).collect(toList());
    }
}
