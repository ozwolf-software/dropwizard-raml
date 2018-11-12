package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;

@JsonSerialize
@JsonPropertyOrder({"displayName", "description", "uriParameters"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlResourceModel {
    private final String path;
    private final String displayName;
    private final String description;
    private final int displayOrder;
    private final Map<String, RamlParameterModel> uriParameters;
    private final Map<String, RamlMethodModel> methods;
    private final Map<String, RamlResourceModel> resources;

    public RamlResourceModel(String path,
                             String displayName,
                             String description,
                             int displayOrder,
                             Map<String, RamlParameterModel> uriParameters) {
        this.path = path;
        this.displayName = displayName;
        this.description = description;
        this.displayOrder = displayOrder;
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

    @JsonIgnore
    public int getDisplayOrder() {
        return displayOrder;
    }

    @JsonProperty("uriParameters")
    public Map<String, RamlParameterModel> getUriParameters() {
        return nullIfEmpty(uriParameters);
    }

    @JsonAnyGetter
    public Map<String, Object> getChildren() {
        Map<String, Object> result = newHashMap();
        result.putAll(methods);
        result.putAll(resources);
        return result;
    }

    @JsonIgnore
    public Map<String, RamlMethodModel> getMethods() {
        return methods;
    }

    @JsonIgnore
    public Map<String, RamlResourceModel> getResources() {
        return resources;
    }

    public void addMethod(RamlMethodModel method) {
        this.methods.put(method.getAction(), method);
    }

    public void addSubResource(RamlResourceModel resource) {
        this.resources.put(resource.getPath(), resource);
    }
}
