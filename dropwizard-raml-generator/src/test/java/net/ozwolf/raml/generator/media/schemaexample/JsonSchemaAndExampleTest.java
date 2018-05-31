package net.ozwolf.raml.generator.media.schemaexample;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.generator.MediaMappers;
import net.ozwolf.raml.generator.model.SchemaAndExample;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonSchemaAndExampleTest {
    @AfterEach
    void tearDown() {
        MediaMappers.instance().reset();
    }

    @Test
    void shouldCreateSchemaAndExampleFromAnnotationAndClass() throws JSONException {
        RamlBody annotation = mock(RamlBody.class);
        when(annotation.contentType()).thenReturn("application/json");
        when(annotation.type()).then(i -> TestResponse.class);
        when(annotation.schema()).thenReturn("");
        when(annotation.example()).thenReturn("");

        SchemaAndExample result = new JsonSchemaAndExample().generate(annotation);

        String schema = result.getSchema().orElse(null);
        String example = result.getExample().orElse(null);

        JSONAssert.assertEquals(fixture("fixtures/json-schema-example/schema.json"), schema, true);
        JSONAssert.assertEquals(fixture("fixtures/json-schema-example/example.json"), example, true);
    }

    @Test
    void shouldCreateSchemaAndExampleFromType() throws JSONException {
        SchemaAndExample result = new JsonSchemaAndExample().generate(TestResponse.class);

        String schema = result.getSchema().orElse(null);
        String example = result.getExample().orElse(null);

        JSONAssert.assertEquals(fixture("fixtures/json-schema-example/schema.json"), schema, true);
        JSONAssert.assertEquals(fixture("fixtures/json-schema-example/example.json"), example, true);
    }

    @JsonSerialize
    @JsonPropertyOrder({"self", "id", "name", "startDate", "endDate", "people"})
    @JsonSchemaDescription("the test response")
    @JsonSchemaTitle("Test Response")
    public static class TestResponse {
        private final URI self;
        private final Integer id;
        private final String name;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final List<Person> people;

        public TestResponse(URI self, Integer id, String name, LocalDate startDate, LocalDate endDate, List<Person> people) {
            this.self = self;
            this.id = id;
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
            this.people = people;
        }

        @JsonProperty(value = "self", required = true)
        @JsonPropertyDescription("the reference to this resource")
        public URI getSelf() {
            return self;
        }

        @JsonProperty(value = "id", required = true)
        @JsonPropertyDescription("the response id")
        public Integer getId() {
            return id;
        }

        @JsonProperty(value = "name", required = true)
        @JsonPropertyDescription("the name")
        public String getName() {
            return name;
        }

        @JsonProperty(value = "startDate", required = true)
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonSchemaFormat("date")
        @JsonPropertyDescription("the start date")
        public LocalDate getStartDate() {
            return startDate;
        }

        @JsonProperty(value = "endDate", required = true)
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonSchemaFormat("date")
        @JsonPropertyDescription("the end date")
        public LocalDate getEndDate() {
            return endDate;
        }

        @JsonProperty(value = "people", required = true)
        @JsonPropertyDescription("the people for this response")
        public List<Person> getPeople() {
            return people;
        }

        @RamlExample
        public static TestResponse example() {
            return new TestResponse(
                    URI.create("/test-response/1"),
                    1,
                    "Test Response 1",
                    LocalDate.parse("2018-01-01"),
                    LocalDate.parse("2018-05-01"),
                    newArrayList(
                            new Person(
                                    1,
                                    "Smith",
                                    "John",
                                    new Address(
                                            "123 Main Street",
                                            "Sydney",
                                            "NSW",
                                            "2000",
                                            "AU"
                                    )
                            ),
                            new Person(
                                    2,
                                    "Rogers",
                                    "Billy",
                                    new Address(
                                            "1000 Center Street",
                                            "Sydney",
                                            "NSW",
                                            "2000",
                                            "AU"
                                    )
                            )
                    )
            );
        }
    }

    @JsonSerialize
    @JsonPropertyOrder({"id", "lastName", "givenNames", "address"})
    @JsonSchemaDescription("a person entity")
    @JsonSchemaTitle("Person Response")
    public static class Person {
        private final Integer id;
        private final String lastName;
        private final String givenNames;
        private final Address address;

        public Person(Integer id, String lastName, String givenNames, Address address) {
            this.id = id;
            this.lastName = lastName;
            this.givenNames = givenNames;
            this.address = address;
        }

        @JsonProperty(value = "id", required = true)
        @JsonPropertyDescription("the identifier of the person")
        public Integer getId() {
            return id;
        }

        @JsonProperty(value = "lastName", required = true)
        @JsonPropertyDescription("the person's last name")
        public String getLastName() {
            return lastName;
        }

        @JsonProperty(value = "givenNames", required = true)
        @JsonPropertyDescription("the person's given names")
        public String getGivenNames() {
            return givenNames;
        }

        @JsonProperty(value = "address", required = true)
        @JsonPropertyDescription("the person's address")
        public Address getAddress() {
            return address;
        }
    }

    @JsonSerialize
    @JsonPropertyOrder({"street", "suburb", "state", "postCode", "country"})
    @JsonSchemaDescription("a postal or street address")
    @JsonSchemaTitle("Address Response")
    public static class Address {
        private final String street;
        private final String suburb;
        private final String state;
        private final String postCode;
        private final String country;

        public Address(String street, String suburb, String state, String postCode, String country) {
            this.street = street;
            this.suburb = suburb;
            this.state = state;
            this.postCode = postCode;
            this.country = country;
        }

        @JsonProperty(value = "street", required = true)
        @JsonPropertyDescription("the street number and name")
        public String getStreet() {
            return street;
        }

        @JsonProperty(value = "suburb", required = true)
        @JsonPropertyDescription("the suburb")
        public String getSuburb() {
            return suburb;
        }

        @JsonProperty(value = "state", required = true)
        @JsonPropertyDescription("the state")
        public String getState() {
            return state;
        }

        @JsonProperty(value = "postCode", required = true)
        @JsonPropertyDescription("the post code")
        public String getPostCode() {
            return postCode;
        }

        @JsonProperty(value = "country", required = true)
        @JsonPropertyDescription("the country")
        public String getCountry() {
            return country;
        }
    }
}