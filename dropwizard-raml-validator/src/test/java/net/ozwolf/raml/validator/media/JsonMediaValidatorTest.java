package net.ozwolf.raml.validator.media;

import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.junit.Test;

import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static net.ozwolf.raml.validator.testutil.SpecificationViolationConditions.violationOf;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonMediaValidatorTest {
    @Test
    public void shouldPassJsonMediaValidation() {
        Node node = new Node("application/json");
        String jsonSchema = fixture("fixtures/media/json/json-schema.json");
        String content = fixture("fixtures/media/json/valid-json-body.json");

        List<SpecificationViolation> result = JsonMediaValidator.newInstance().validate(node, jsonSchema, content.getBytes());

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFailJsonMediaValidation() {
        Node node = new Node(new Node("application/json"), "content");
        String jsonSchema = fixture("fixtures/media/json/json-schema.json");

        String content = fixture("fixtures/media/json/invalid-json-body.json");

        List<SpecificationViolation> result = JsonMediaValidator.newInstance().validate(node, jsonSchema, content.getBytes());

        assertThat(result)
                .hasSize(3)
                .areAtLeastOne(violationOf("application/json.content.genre", "instance value (\"Unknown\") not found in enum (possible values: [\"Horror\",\"SciFi\",\"Fantasy\",\"Romance\",\"Action\",\"NonFiction\"])"))
                .areAtLeastOne(violationOf("application/json.content.authorId", "instance type (string) does not match any allowed primitive type (allowed: [\"integer\"])"))
                .areAtLeastOne(violationOf("application/json.content.publishDate", "ECMA 262 regex \"([12]\\d{3})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\" does not match input string \"01/12/2018\""));
    }
}