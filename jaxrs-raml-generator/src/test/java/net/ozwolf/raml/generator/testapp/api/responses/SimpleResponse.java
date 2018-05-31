package net.ozwolf.raml.generator.testapp.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.annotations.RamlExample;

@JsonSerialize
@JsonPropertyOrder("random")
@JsonSchemaTitle("Simple Response")
@JsonSchemaDescription("a simple random response")
public class SimpleResponse {
    private final String random;

    public SimpleResponse(String random) {
        this.random = random;
    }

    @JsonProperty(value = "random", required = true)
    @JsonSchemaDescription("the randomly generated value")
    public String getRandom() {
        return random;
    }

    @RamlExample
    public static SimpleResponse example() {
        return new SimpleResponse("ARANDOMSTRING");
    }
}
