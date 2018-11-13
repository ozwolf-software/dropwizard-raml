package net.ozwolf.raml.generator.conditions;

import org.assertj.core.api.Condition;
import org.raml.v2.api.model.v10.bodies.Response;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

public class ResponseCondition extends Condition<Response> {
    private final Integer status;
    private final String description;

    private final List<AbstractParameterCondition> headers;
    private final List<BodyCondition> bodies;

    public ResponseCondition(Integer status, String description) {
        this.status = status;
        this.description = description;
        this.headers = newArrayList();
        this.bodies = newArrayList();
    }

    public ResponseCondition(Integer status) {
        this(status, null);
    }

    public ResponseCondition withHeader(AbstractParameterCondition header) {
        this.headers.add(header);
        return this;
    }

    public ResponseCondition withBody(BodyCondition body) {
        this.bodies.add(body);
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public boolean matches(Response value) {
        if (!String.valueOf(status).equals(value.code().value()))
            return false;

        if (description != null && Optional.ofNullable(value.description()).map(v -> !v.value().equals(description)).orElse(true))
            return false;

        if (!AbstractParameterCondition.match(headers, value.headers()))
            return false;

        if (!BodyCondition.matches(bodies, value.body()))
            return false;

        return true;
    }

    public static boolean matches(List<ResponseCondition> expectedResponses, List<Response> actualResponses){
        for(ResponseCondition response : expectedResponses){
            Response actual = actualResponses.stream().filter(r -> String.valueOf(response.getStatus()).equals(r.code().value())).findFirst().orElse(null);
            if (actual == null || !response.matches(actual))
                return false;
        }

        return true;
    }
}
