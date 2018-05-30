package net.ozwolf.raml.generator.testapp.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.generator.testapp.resources.ComplexResource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@JsonSerialize
@JsonPropertyOrder({"self", "id", "name", "address"})
public class PersonResponse {
    private final URI self;
    private final String id;
    private final String name;
    private final AddressResponse address;

    public PersonResponse(String complexId, String id, String name, AddressResponse address) {
        this.self = UriBuilder.fromResource(ComplexResource.class).path(ComplexResource.class, "getPerson").build(complexId, id);
        this.id = id;
        this.name = name;
        this.address = address;
    }

    @JsonProperty(value = "self", required = true)
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "id", required = true)
    public String getId() {
        return id;
    }

    @JsonProperty(value = "name", required = true)
    public String getName() {
        return name;
    }

    @JsonProperty(value = "address", required = true)
    public AddressResponse getAddress() {
        return address;
    }

    @RamlExample
    public static PersonResponse example(){
        return new PersonResponse(
                "complex-1",
                "person-2",
                "Test Person",
                new AddressResponse(
                        "123 Main Street",
                        "Old Town",
                        "Tasmania",
                        "7000",
                        "AU"
                )
        );
    }
}
