package net.ozwolf.raml.generator.testapp.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonPropertyOrder({"street", "suburb", "state", "postCode", "country"})
public class AddressResponse {
    private final String street;
    private final String suburb;
    private final String state;
    private final String postCode;
    private final String country;

    public AddressResponse(String street, String suburb, String state, String postCode, String country) {
        this.street = street;
        this.suburb = suburb;
        this.state = state;
        this.postCode = postCode;
        this.country = country;
    }

    @JsonProperty(value = "street", required = true)
    public String getStreet() {
        return street;
    }

    @JsonProperty(value = "suburb", required = true)
    public String getSuburb() {
        return suburb;
    }

    @JsonProperty(value = "state", required = true)
    public String getState() {
        return state;
    }

    @JsonProperty(value = "postCode", required = true)
    public String getPostCode() {
        return postCode;
    }

    @JsonProperty(value = "country", required = true)
    public String getCountry() {
        return country;
    }
}
