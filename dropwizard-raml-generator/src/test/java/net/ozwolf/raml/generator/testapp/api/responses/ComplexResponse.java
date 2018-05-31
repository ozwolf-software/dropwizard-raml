package net.ozwolf.raml.generator.testapp.api.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.generator.testapp.resources.ComplexResource;
import org.joda.time.LocalDate;

import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@JsonSerialize
@JsonPropertyOrder({"self", "id", "dateFor", "testContext", "scale", "people"})
@JsonSchemaDescription("a complex response type")
public class ComplexResponse {
    private final URI self;
    private final String id;
    private final LocalDate dateFor;
    private final String testContext;
    private final BigDecimal scale;
    private final List<PersonReferenceResponse> people;

    public ComplexResponse(String id, LocalDate dateFor, String testContext, List<PersonReferenceResponse> people) {
        this.self = UriBuilder.fromResource(ComplexResource.class).build(id);
        this.id = id;
        this.dateFor = dateFor;
        this.testContext = testContext;
        this.scale = new BigDecimal("1234.52893");
        this.people = people;
    }

    @JsonProperty(value = "self", required = true)
    @JsonSchemaDescription("the reference to this resource")
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "id", required = true)
    @JsonSchemaDescription("the complex id")
    public String getId() {
        return id;
    }

    @JsonProperty(value = "dateFor", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSchemaDescription("the date for object is for")
    public LocalDate getDateFor() {
        return dateFor;
    }

    @JsonProperty(value = "testContext", required = true)
    @JsonSchemaDescription("the test context")
    public String getTestContext() {
        return testContext;
    }

    @JsonProperty(value = "scale", required = true)
    public BigDecimal getScale() {
        return scale;
    }

    @JsonProperty(value = "people", required = true)
    @JsonSchemaDescription("the people related to this item for the given date")
    public List<PersonReferenceResponse> getPeople() {
        return people;
    }

    @RamlExample
    public static ComplexResponse example() {
        return new ComplexResponse(
                "complex-1",
                LocalDate.parse("2018-01-01"),
                "context-test",
                newArrayList(
                        new PersonReferenceResponse("complex-1", "person-1", "Test Person 1"),
                        new PersonReferenceResponse("complex-1", "person-2", "Test Person 2")
                )
        );
    }
}
