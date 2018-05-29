package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlParameter;
import net.ozwolf.raml.generator.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.StringUtils.trimToNull;

@JsonSerialize
@JsonPropertyOrder({"displayName", "description", "type", "required", "multiple", "allowedValues", "example", "pattern", "minimum", "maximum"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlParameterModel {
    private final String displayName;
    private final String description;
    private final String type;
    private final boolean required;
    private final boolean multiple;
    private final Set<String> allowedValues;
    private final String example;
    private final String pattern;
    private final int minimum;
    private final int maximum;

    public RamlParameterModel(RamlParameter annotation){
        this.displayName = annotation.displayName();
        this.description = annotation.description();
        this.type = annotation.type();
        this.required = annotation.required();
        this.multiple = annotation.multiple();
        this.allowedValues = newHashSet(annotation.allowedValues());
        this.example = annotation.example();
        this.pattern = annotation.pattern();
        this.minimum = annotation.minimum();
        this.maximum = annotation.maximum();
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return trimToNull(displayName);
    }

    @JsonProperty("description")
    public String getDescription() {
        return trimToNull(description);
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("required")
    public boolean isRequired() {
        return required;
    }

    @JsonProperty("multiple")
    public boolean isMultiple() {
        return multiple;
    }

    @JsonProperty("allowedValues")
    public Set<String> getAllowedValues() {
        return CollectionUtils.nullIfEmpty(allowedValues);
    }

    @JsonProperty("example")
    public String getExample() {
        return trimToNull(example);
    }

    @JsonProperty("pattern")
    public String getPattern() {
        return trimToNull(pattern);
    }

    @JsonProperty("minimum")
    public Integer getMinimum() {
        return minimum == Integer.MIN_VALUE ? null : minimum;
    }

    @JsonProperty("maximum")
    public Integer getMaximum() {
        return maximum == Integer.MIN_VALUE ? null : maximum;
    }
}
