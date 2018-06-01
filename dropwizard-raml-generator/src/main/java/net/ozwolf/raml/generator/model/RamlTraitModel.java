package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlTrait;

import static org.apache.commons.lang.StringUtils.trimToNull;


@JsonSerialize
@JsonPropertyOrder({"description", "headers", "queryParameters", "responses"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlTraitModel extends RamlDescribedByModel {
    private final String description;

    public RamlTraitModel(RamlTrait trait) {
        super(trait.describedBy());
        this.description = trait.description();
    }

    @JsonProperty("description")
    public String getDescription() {
        return trimToNull(description);
    }
}
