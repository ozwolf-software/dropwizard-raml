package net.ozwolf.raml.validator.media;

import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.junit.Test;

import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static net.ozwolf.raml.validator.testutil.SpecificationViolationConditions.violationOf;
import static org.assertj.core.api.Assertions.assertThat;

public class XmlMediaValidatorTest {
    @Test
    public void shouldPassXmlValidation() {
        Node node = new Node(new Node("application/xml"), "content");
        String xsd = fixture("fixtures/media/xml/xml-schema.xsd");
        String content = fixture("fixtures/media/xml/valid-xml-body.xml");

        List<SpecificationViolation> result = XmlMediaValidator.newInstance().validate(node, xsd, content.getBytes());

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFailXmlValidation() {
        Node node = new Node(new Node("application/xml"), "content");
        String xsd = fixture("fixtures/media/xml/xml-schema.xsd");
        String content = fixture("fixtures/media/xml/invalid-xml-body.xml");

        List<SpecificationViolation> result = XmlMediaValidator.newInstance().validate(node, xsd, content.getBytes());

        assertThat(result)
                .hasSize(7)
                .areAtLeastOne(violationOf("application/xml.content.line-3.[0]", "cvc-complex-type.2.4.a: Invalid content was found starting with element 'genre'. One of '{title}' is expected."))
                .areAtLeastOne(violationOf("application/xml.content.line-3.[1]", "cvc-enumeration-valid: Value 'Unknown' is not facet-valid with respect to enumeration '[SciFi, Fantasy, Romance, Action, NonFiction]'. It must be a value from the enumeration."))
                .areAtLeastOne(violationOf("application/xml.content.line-3.[2]", "cvc-type.3.1.3: The value 'Unknown' of element 'genre' is not valid."))
                .areAtLeastOne(violationOf("application/xml.content.line-4.[0]", "cvc-datatype-valid.1.2.1: '01/12/2018' is not a valid value for 'date'."))
                .areAtLeastOne(violationOf("application/xml.content.line-4.[1]", "cvc-type.3.1.3: The value '01/12/2018' of element 'publishDate' is not valid."))
                .areAtLeastOne(violationOf("application/xml.content.line-5.[0]", "cvc-datatype-valid.1.2.1: 'ABC123' is not a valid value for 'integer'."))
                .areAtLeastOne(violationOf("application/xml.content.line-5.[1]", "cvc-type.3.1.3: The value 'ABC123' of element 'authorId' is not valid."));
    }
}