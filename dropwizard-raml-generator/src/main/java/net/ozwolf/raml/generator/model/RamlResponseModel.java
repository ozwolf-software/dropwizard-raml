package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.annotations.RamlResponse;
import net.ozwolf.raml.generator.util.CollectionUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

@JsonSerialize
@JsonPropertyOrder({"description", "body"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlResponseModel {
    private final String description;
    private final Map<String, RamlBodyModel> body;

    public RamlResponseModel(RamlResponse annotation) {
        this.description = annotation.description();
        this.body = Arrays.stream(annotation.bodies()).collect(toMap(RamlBody::contentType, RamlBodyModel::new));
    }

    public RamlResponseModel(Set<String> contentTypes) {
        this.description = "auto generated responses";
        this.body = contentTypes.stream().collect(toMap(v -> v, v -> new RamlBodyModel()));
    }

    public RamlResponseModel(Set<String> contentTypes, Class<?> type) {
        this.description = "auto generated responses";
        this.body = contentTypes.stream().collect(toMap(v -> v, v -> new RamlBodyModel(v, type)));
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("body")
    public Map<String, RamlBodyModel> getBody() {
        return CollectionUtils.nullIfEmpty(body);
    }
}
