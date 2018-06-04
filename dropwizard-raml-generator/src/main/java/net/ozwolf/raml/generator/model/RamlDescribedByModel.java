package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlDescribedBy;
import net.ozwolf.raml.annotations.RamlParameter;
import net.ozwolf.raml.annotations.RamlResponse;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;

@JsonSerialize
@JsonPropertyOrder({"headers", "queryParameters", "responses"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlDescribedByModel {
    private final Map<String, RamlParameterModel> headers;
    private final Map<String, RamlParameterModel> queryParameters;
    private final Map<Integer, RamlResponseModel> responses;

    public RamlDescribedByModel(RamlDescribedBy annotation) {
        this.headers = Arrays.stream(annotation.headers()).collect(toMap(RamlParameter::name, RamlParameterModel::new));
        this.queryParameters = Arrays.stream(annotation.queryParameters()).collect(toMap(RamlParameter::name, RamlParameterModel::new));
        this.responses = Arrays.stream(annotation.responses()).collect(toMap(RamlResponse::status, RamlResponseModel::new));
    }

    @JsonProperty("headers")
    public Map<String, RamlParameterModel> getHeaders() {
        return nullIfEmpty(headers);
    }

    @JsonProperty("queryParameters")
    public Map<String, RamlParameterModel> getQueryParameters() {
        return nullIfEmpty(queryParameters);
    }

    @JsonProperty("responses")
    public Map<Integer, RamlResponseModel> getResponses() {
        return nullIfEmpty(responses);
    }
}
