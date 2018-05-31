package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlDescription;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.annotations.RamlParameter;
import net.ozwolf.raml.generator.util.CollectionUtils;
import net.ozwolf.raml.generator.util.ParameterUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.trimToNull;

@JsonSerialize
@JsonPropertyOrder({"displayName", "description", "type", "required", "items", "default", "enum", "example", "pattern", "minimum", "maximum"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlParameterModel {
    private final String name;
    private final String displayName;
    private final String description;
    private final String type;
    private final boolean required;
    private final ArrayItemsModel items;
    private final String defaultValue;
    private final Set<String> allowedValues;
    private final String example;
    private final String pattern;
    private final Long minimum;
    private final Long maximum;

    public RamlParameterModel(RamlParameter annotation) {
        this.name = annotation.name();
        this.displayName = annotation.displayName();
        this.description = annotation.description();
        this.type = annotation.multiple() ? "array" : annotation.type();
        this.required = annotation.required();
        this.items = annotation.multiple() ? new ArrayItemsModel(annotation) : null;
        this.defaultValue = annotation.multiple() ? null : annotation.defaultValue();
        this.allowedValues = annotation.multiple() ? null : ParameterUtils.getAllowedValues(annotation);
        this.example = annotation.multiple() ? null : annotation.example();
        this.pattern = annotation.multiple() ? null : annotation.pattern();
        this.minimum = annotation.multiple() ? null : annotation.minimum();
        this.maximum = annotation.multiple() ? null : annotation.maximum();
    }

    public RamlParameterModel(String name, Parameter parameter) {
        boolean multiple = parameter.getType().isArray() || Collection.class.isAssignableFrom(parameter.getType());
        this.name = name;
        this.displayName = null;
        this.description = Optional.ofNullable(parameter.getAnnotation(RamlDescription.class)).map(RamlDescription::value).orElse(null);
        this.type = multiple ? "array" : RamlParameterType.getRamlType(parameter);
        this.required = parameter.isAnnotationPresent(NotNull.class);
        this.items = multiple ? new ArrayItemsModel(parameter) : null;
        this.defaultValue = multiple ? null : Optional.ofNullable(parameter.getAnnotation(DefaultValue.class)).map(DefaultValue::value).orElse(null);
        this.allowedValues = multiple ? null : ParameterUtils.getAllowedValues(parameter);
        this.example = multiple ? null : Optional.ofNullable(parameter.getAnnotation(RamlExample.class)).map(v -> trimToNull(v.value())).orElse(null);
        this.pattern = multiple ? null : Optional.ofNullable(parameter.getAnnotation(Pattern.class)).map(Pattern::regexp).orElse(null);
        this.minimum = multiple ? null : Optional.ofNullable(parameter.getAnnotation(Min.class)).map(Min::value).orElse(null);
        this.maximum = multiple ? null : Optional.ofNullable(parameter.getAnnotation(Max.class)).map(Max::value).orElse(null);
    }

    @JsonIgnore
    public String getName() {
        return name;
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

    @JsonProperty("items")
    public ArrayItemsModel getItems() {
        return items;
    }

    @JsonProperty("default")
    public String getDefaultValue() {
        return trimToNull(defaultValue);
    }

    @JsonProperty("enum")
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
    public Long getMinimum() {
        return minimum == null || minimum == Long.MIN_VALUE ? null : minimum;
    }

    @JsonProperty("maximum")
    public Long getMaximum() {
        return maximum == null || maximum == Long.MIN_VALUE ? null : maximum;
    }

    @JsonSerialize
    @JsonPropertyOrder({"type", "enum", "example", "pattern", "minimum", "maximum"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ArrayItemsModel {
        private final String type;
        private final Set<String> allowedValues;
        private final String example;
        private final String pattern;
        private final Long minimum;
        private final Long maximum;

        private ArrayItemsModel(RamlParameter annotation) {
            this.type = annotation.type();
            this.allowedValues = ParameterUtils.getAllowedValues(annotation);
            this.example = annotation.example();
            this.pattern = annotation.pattern();
            this.minimum = annotation.minimum();
            this.maximum = annotation.maximum();
        }

        private ArrayItemsModel(Parameter parameter) {
            this.type = RamlParameterType.getRamlType(parameter);
            this.allowedValues = ParameterUtils.getAllowedValues(parameter);
            this.example = Optional.ofNullable(parameter.getAnnotation(RamlExample.class)).map(v -> trimToNull(v.value())).orElse(null);
            this.pattern = Optional.ofNullable(parameter.getAnnotation(Pattern.class)).map(Pattern::regexp).orElse(null);
            this.minimum = Optional.ofNullable(parameter.getAnnotation(Min.class)).map(Min::value).orElse(null);
            this.maximum = Optional.ofNullable(parameter.getAnnotation(Max.class)).map(Max::value).orElse(null);
        }

        @JsonProperty("type")
        public String getType() {
            return type;
        }

        @JsonProperty("enum")
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
        public Long getMinimum() {
            return minimum == null || minimum == Long.MIN_VALUE ? null : minimum;
        }

        @JsonProperty("maximum")
        public Long getMaximum() {
            return maximum == null || maximum == Long.MIN_VALUE ? null : maximum;
        }
    }
}
