package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.annotations.RamlParameter;
import net.ozwolf.raml.annotations.RamlResponse;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;

@JsonSerialize
@JsonPropertyOrder({"description", "body"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlResponseModel {
    private final Integer status;
    private final String description;
    private final Map<String, RamlParameterModel> headers;
    private final Map<String, RamlBodyModel> body;

    public RamlResponseModel(RamlResponse annotation) {
        this.status = annotation.status();
        this.description = annotation.description();
        this.headers = Arrays.stream(annotation.headers()).collect(toMap(RamlParameter::name, RamlParameterModel::new));
        this.body = Arrays.stream(annotation.bodies()).collect(toMap(RamlBody::contentType, RamlBodyModel::new));
    }

    public RamlResponseModel(Integer status, Set<String> contentTypes) {
        this.status = status;
        this.description = "auto generated responses";
        this.headers = newHashMap();
        this.body = contentTypes.stream().collect(toMap(v -> v, RamlBodyModel::new));
    }

    public RamlResponseModel(Integer status, Set<String> contentTypes, Class<?> type) {
        this.status = status;
        this.description = "auto generated responses";
        this.headers = newHashMap();
        this.body = contentTypes.stream().collect(toMap(v -> v, v -> new RamlBodyModel(v, type)));
    }

    @JsonIgnore
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("headers")
    public Map<String, RamlParameterModel> getHeaders() {
        return nullIfEmpty(headers);
    }

    @JsonProperty("body")
    public Map<String, RamlBodyModel> getBody() {
        return nullIfEmpty(body);
    }
}
