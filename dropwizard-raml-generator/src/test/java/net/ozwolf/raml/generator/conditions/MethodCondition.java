package net.ozwolf.raml.generator.conditions;

import org.assertj.core.api.Condition;
import org.raml.v2.api.model.v10.methods.Method;
import org.raml.v2.api.model.v10.security.SecurityScheme;
import org.raml.v2.api.model.v10.security.SecuritySchemeRef;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

public class MethodCondition extends Condition<Method> {
    private final String method;
    private final String description;

    private final List<String> securedBy;

    private final List<AbstractParameterCondition> queryParameters;
    private final List<AbstractParameterCondition> headers;

    private final List<BodyCondition> requests;
    private final List<ResponseCondition> responses;

    public MethodCondition(String method, String description) {
        super("<method=" + method + ", description=" + description + ">");
        this.method = method;
        this.description = description;

        this.queryParameters = newArrayList();
        this.headers = newArrayList();

        this.securedBy = newArrayList();
        this.requests = newArrayList();
        this.responses = newArrayList();
    }

    public MethodCondition(String method) {
        this(method, null);
    }

    public MethodCondition securedBy(String key){
        this.securedBy.add(key);
        return this;
    }

    public MethodCondition withQueryParameter(AbstractParameterCondition parameter) {
        this.queryParameters.add(parameter);
        return this;
    }

    public MethodCondition withHeader(AbstractParameterCondition header) {
        this.headers.add(header);
        return this;
    }

    public MethodCondition withRequestBody(BodyCondition request) {
        this.requests.add(request);
        return this;
    }

    public MethodCondition withResponse(ResponseCondition response) {
        this.responses.add(response);
        return this;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean matches(Method value) {
        if (!method.equalsIgnoreCase(value.method()))
            return false;

        if (description != null && Optional.ofNullable(value.description()).map(v -> !v.value().equals(description)).orElse(true))
            return false;

        for(String securityKey: securedBy){
            SecuritySchemeRef actual = value.securedBy().stream().filter(s -> s.name().equals(securityKey)).findFirst().orElse(null);
            if (actual == null)
                return false;
        }

        if (!AbstractParameterCondition.match(queryParameters, value.queryParameters()))
            return false;

        if (!AbstractParameterCondition.match(headers, value.headers()))
            return false;

        if (!BodyCondition.matches(requests, value.body()))
            return false;

        if (!ResponseCondition.matches(responses, value.responses()))
            return false;

        return true;
    }

    public static boolean matches(List<MethodCondition> expectedMethods, List<Method> actualMethods){
        for(MethodCondition method : expectedMethods){
            Method actual = actualMethods.stream().filter(m -> m.method().equalsIgnoreCase(method.getMethod())).findFirst().orElse(null);
            if (actual == null || !method.matches(actual))
                return false;
        }

        return true;
    }
}
