package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlDescriptor;
import net.ozwolf.raml.annotations.RamlParameter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;

@JsonSerialize
@JsonPropertyOrder({"headers", "queryParameters", "responses"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlDescribedByModel {
    private final Map<String, RamlParameterModel> headers;
    private final Map<String, RamlParameterModel> queryParameters;
    private final Map<Integer, RamlResponseModel> responses;

    public RamlDescribedByModel(RamlDescriptor annotation) {
        this.headers = Arrays.stream(annotation.headers()).collect(toMap(RamlParameter::name, RamlParameterModel::new));
        this.queryParameters = Arrays.stream(annotation.queryParameters()).collect(toMap(RamlParameter::name, RamlParameterModel::new));
        this.responses = Arrays.stream(annotation.responses()).map(RamlResponseModel::new).collect(toList());
    }

    @JsonProperty("headers")
    public List<RamlParameterModel> getHeaders() {
        return nullIfEmpty(headers);
    }

    @JsonProperty("queryParameters")
    public List<RamlParameterModel> getQueryParameters() {
        return nullIfEmpty(queryParameters);
    }

    @JsonProperty("responses")
    public List<RamlResponseModel> getResponses() {
        return nullIfEmpty(responses);
    }
}
