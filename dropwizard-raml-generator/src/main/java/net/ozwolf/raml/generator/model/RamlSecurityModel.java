package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlSecurity;

import static org.apache.commons.lang.StringUtils.trimToNull;

@JsonSerialize
@JsonPropertyOrder({"type", "description", "describedBy"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlSecurityModel {
    private final String type;
    private final String description;
    private final RamlDescribedByModel describedBy;

    public RamlSecurityModel(RamlSecurity annotation) {
        this.type = annotation.type();
        this.description = annotation.description();
        this.describedBy = new RamlDescribedByModel(annotation.describedBy());
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonPropertyOrder("description")
    public String getDescription() {
        return trimToNull(description);
    }

    @JsonProperty("describedBy")
    public RamlDescribedByModel getDescribedBy() {
        return describedBy;
    }
}
