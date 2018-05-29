package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlResponse;
import net.ozwolf.raml.generator.util.CollectionUtils;

import java.util.Arrays;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@JsonSerialize
@JsonPropertyOrder({"description", "body"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlResponseModel {
    private final String description;
    private final Map<String, RamlBodyModel> body;

    public RamlResponseModel(RamlResponse annotation) {
        this.description = annotation.description();
        this.body = newHashMap();
        Arrays.stream(annotation.bodies())
                .forEach(a -> body.put(a.contentType(), new RamlBodyModel(a)));
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
