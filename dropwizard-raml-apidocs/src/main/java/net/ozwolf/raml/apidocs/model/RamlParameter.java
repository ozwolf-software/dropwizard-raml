package net.ozwolf.raml.apidocs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
@JsonPropertyOrder({"name", "type", "displayName", "description", "pattern", "example", "default", "required", "multiple", "allowedValues", "minValue", "maxValue"})
public interface RamlParameter {
    @JsonProperty("name")
    String getName();

    @JsonProperty("type")
    String getType();

    @JsonProperty("displayName")
    String getDisplayName();

    @JsonProperty("description")
    String getDescription();

    @JsonProperty("pattern")
    String getPattern();

    @JsonProperty("example")
    String getExample();

    @JsonProperty("default")
    String getDefault();

    @JsonProperty("required")
    boolean isRequired();

    @JsonProperty("multiple")
    boolean isMultiple();

    @JsonProperty("allowedValues")
    List<String> getAllowedValues();

    @JsonProperty("minValue")
    Double getMinValue();

    @JsonProperty("maxValue")
    Double getMaxValue();
}
