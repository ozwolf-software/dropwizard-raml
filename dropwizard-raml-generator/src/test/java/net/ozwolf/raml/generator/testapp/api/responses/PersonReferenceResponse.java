package net.ozwolf.raml.generator.testapp.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.ozwolf.raml.generator.testapp.resources.ComplexResource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class PersonReferenceResponse {
    private final URI href;
    private final String id;
    private final String name;

    public PersonReferenceResponse(String complexId, String id, String name) {
        this.href = UriBuilder.fromResource(ComplexResource.class).path(ComplexResource.class, "getPerson").build(complexId, id);
        this.id = id;
        this.name = name;
    }

    @JsonProperty(value = "href", required = true)
    public URI getHref() {
        return href;
    }

    @JsonProperty(value = "id", required = true)
    public String getId() {
        return id;
    }

    @JsonProperty(value = "name", required = true)
    public String getName() {
        return name;
    }
}
