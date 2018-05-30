package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;

@JsonSerialize
@JsonPropertyOrder({"displayName", "description", "uriParameters"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlResourceModel {
    private final String path;
    private final String displayName;
    private final String description;
    private final Map<String, RamlParameterModel> uriParameters;
    private final Map<String, RamlMethodModel> methods;
    private final Map<String, RamlResourceModel> resources;

    public RamlResourceModel(String path,
                             String displayName,
                             String description,
                             Map<String, RamlParameterModel> uriParameters) {
        this.path = path;
        this.displayName = displayName;
        this.description = description;
        this.uriParameters = uriParameters;
        this.methods = newHashMap();
        this.resources = newHashMap();
    }

    @JsonIgnore
    public String getPath() {
        return path;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("uriParameters")
    public Map<String, RamlParameterModel> getUriParameters() {
        return nullIfEmpty(uriParameters);
    }

    @JsonAnyGetter
    public Map<String, Object> getMethods() {
        Map<String, Object> result = newHashMap();
        result.putAll(methods);
        result.putAll(resources);
        return result;
    }

    public void addMethod(RamlMethodModel method) {
        this.methods.put(method.getAction(), method);
    }

    public void addSubResource(RamlResourceModel resource) {
        this.resources.put(resource.getPath(), resource);
    }
}
