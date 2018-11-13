package net.ozwolf.raml.generator.conditions;

import org.assertj.core.api.Condition;
import org.raml.v2.api.model.v10.security.SecurityScheme;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

public class SecurityCondition extends Condition<SecurityScheme> {
    private final String key;
    private final String type;
    private final String description;

    private final List<AbstractParameterCondition> headers;
    private final List<AbstractParameterCondition> queryParameters;
    private final List<ResponseCondition> responses;

    public SecurityCondition(String key, String type, String description){
        super("<key=" + key + ", type=" + type + ", description=" + description + ">");

        this.key = key;
        this.type = type;
        this.description = description;
        this.headers = newArrayList();
        this.queryParameters = newArrayList();
        this.responses = newArrayList();
    }

    public SecurityCondition(String key, String type){
        this(key, type, null);
    }

    public SecurityCondition withHeader(AbstractParameterCondition header){
        this.headers.add(header);
        return this;
    }

    public SecurityCondition withQueryParameter(AbstractParameterCondition parameter){
        this.queryParameters.add(parameter);
        return this;
    }

    public SecurityCondition withResponse(ResponseCondition response){
        this.responses.add(response);
        return this;
    }

    @Override
    public boolean matches(SecurityScheme value) {
        if (!key.equals(value.name()) || !type.equals(value.type()))
            return false;

        if (description != null && Optional.ofNullable(value.description()).map(v -> !v.value().equals(description)).orElse(true))
            return false;

        if (!AbstractParameterCondition.match(headers, value.describedBy().headers()))
            return false;

        if (!AbstractParameterCondition.match(queryParameters, value.describedBy().queryParameters()))
            return false;

        if (!ResponseCondition.matches(responses, value.describedBy().responses()))
            return false;

        return true;
    }
}
