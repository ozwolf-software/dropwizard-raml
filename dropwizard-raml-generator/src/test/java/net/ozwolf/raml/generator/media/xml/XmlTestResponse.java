package net.ozwolf.raml.generator.media.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import net.ozwolf.raml.annotations.RamlExample;
import org.joda.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@JsonSerialize
@JsonPropertyOrder({"self", "id", "name", "startDate", "endDate", "people"})

@JacksonXmlRootElement(localName = "test")
public class XmlTestResponse {
    private final URI self;
    private final Integer id;
    private final String name;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<Person> people;

    public XmlTestResponse(URI self, Integer id, String name, LocalDate startDate, LocalDate endDate, List<Person> people) {
        this.self = self;
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.people = people;
    }

    @JsonProperty(value = "self", required = true)
    @JacksonXmlProperty(isAttribute = true)
    public URI getSelf() {
        return self;
    }

    @JsonProperty(value = "id", required = true)
    @JacksonXmlProperty(isAttribute = true)
    public Integer getId() {
        return id;
    }

    @JsonProperty(value = "name", required = true)
    public String getName() {
        return name;
    }

    @JsonProperty(value = "startDate", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getStartDate() {
        return startDate;
    }

    @JsonProperty(value = "endDate", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getEndDate() {
        return endDate;
    }

    @JsonProperty(value = "person", required = true)
    @JacksonXmlElementWrapper(localName = "people")
    public List<Person> getPeople() {
        return people;
    }

    @RamlExample
    public static XmlTestResponse example() {
        return new XmlTestResponse(
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

    @JsonSerialize
    @JsonPropertyOrder({"id", "lastName", "givenNames", "address"})
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
        @JacksonXmlProperty(isAttribute = true)
        public Integer getId() {
            return id;
        }

        @JsonProperty(value = "lastName", required = true)
        @JacksonXmlProperty(isAttribute = true)
        public String getLastName() {
            return lastName;
        }

        @JsonProperty(value = "givenNames", required = true)
        @JacksonXmlProperty(isAttribute = true)
        public String getGivenNames() {
            return givenNames;
        }

        @JsonProperty(value = "address", required = true)
        public Address getAddress() {
            return address;
        }
    }

    @JsonSerialize
    @JsonPropertyOrder({"street", "suburb", "state", "postCode", "country"})
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
        @JacksonXmlProperty(isAttribute = true)
        public String getStreet() {
            return street;
        }

        @JsonProperty(value = "suburb", required = true)
        @JacksonXmlProperty(isAttribute = true)
        public String getSuburb() {
            return suburb;
        }

        @JsonProperty(value = "state", required = true)
        @JacksonXmlProperty(isAttribute = true)
        public String getState() {
            return state;
        }

        @JsonProperty(value = "postCode", required = true)
        @JacksonXmlProperty(isAttribute = true)
        public String getPostCode() {
            return postCode;
        }

        @JsonProperty(value = "country", required = true)
        @JacksonXmlProperty(isAttribute = true)
        public String getCountry() {
            return country;
        }
    }
}
