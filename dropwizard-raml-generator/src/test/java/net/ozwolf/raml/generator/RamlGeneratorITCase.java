package net.ozwolf.raml.generator;

import com.google.common.base.Joiner;
import net.ozwolf.raml.generator.conditions.*;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import net.ozwolf.raml.generator.media.json.JsonSchemaFactory;
import net.ozwolf.raml.generator.util.RamlMediaRule;
import net.ozwolf.raml.test.jackson.BigDecimalModule;
import org.junit.ClassRule;
import org.junit.Test;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.api.model.v10.api.Api;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class RamlGeneratorITCase {
    @ClassRule
    public final static RamlMediaRule RAML_MEDIA = new RamlMediaRule();

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldGenerateValidRAML() throws RamlGenerationException {
        RamlMedia.registerModuleFor("application/json", new BigDecimalModule());
        JsonSchemaFactory.registerRemappedType(BigDecimal.class, String.class);

        RamlGenerator generator = new RamlGenerator("net.ozwolf.raml.test", "1.2.3");

        String raml = generator.generate();

        RamlModelResult result = new RamlModelBuilder().buildApi(raml, "/");

        List<ValidationResult> errors = result.getValidationResults();

        assertThat(errors)
                .describedAs(makeError(errors))
                .isEmpty();

        assertThat(result.isVersion10()).isTrue();

        Api api = result.getApiV10();
        assertThat(api.title().value()).isEqualTo("DropWizard Example App");
        assertThat(api.version().value()).isEqualTo("1.2.3");

        assertThat(api.securitySchemes())
                .hasSize(2)
                .areAtLeastOne(
                        new SecurityCondition("user-token", "x-user-token", "a token provided by the applications own security provider")
                                .withHeader(
                                        new StringParameterCondition("Authorization", "used to send a valid user token")
                                                .withPattern("User (.+)")
                                )
                                .withResponse(
                                        new ResponseCondition(401, "user token is not active or expired")
                                                .withBody(
                                                        new BodyCondition("application/json")
                                                                .withSchema(fixture("apispecs/resources/errors/schemas/standard-error-response.json"))
                                                                .withExample(fixture("apispecs/resources/errors/examples/not-authorized-response.json"))
                                                )
                                )
                )
                .areAtLeastOne(new SecurityCondition("oauth2", "OAuth 2.0", "application supports OAuth 2.0 tokens through LinkedIn"));

        assertThat(api.resources())
                .areAtLeastOne(
                        new ResourceCondition("/authors/{id}", "retrieve and update an author")
                                .withUriParameter(
                                        new IntegerParameterCondition("id", "the author id")
                                )
                                .withMethod(
                                        new MethodCondition("GET", "retrieve an author")
                                                .withResponse(new ResponseCondition(404))
                                                .withResponse(
                                                        new ResponseCondition(200, "author retrieved successfully")
                                                                .withBody(
                                                                        new BodyCondition("application/json")
                                                                                .withSchema(fixture("fixtures/raml-model-body-test/author-response-schema.json"))
                                                                                .withExample(fixture("fixtures/raml-model-body-test/author-response-example.json"))
                                                                )
                                                )
                                                .withResponse(
                                                        new ResponseCondition(500, "an internal server error occurred")
                                                                .withBody(
                                                                        new BodyCondition("application/json")
                                                                )
                                                )
                                )
                )
                .areAtLeastOne(
                        new ResourceCondition("/books", "manage books")
                                .withMethod(
                                        new MethodCondition("POST", "create a new book")
                                                .withRequestBody(
                                                        new BodyCondition("application/json")
                                                                .withSchema(fixture("fixtures/raml-model-body-test/book-request-schema.json"))
                                                                .withExample(fixture("fixtures/raml-model-body-test/book-request-example.json"))
                                                )
                                                .withResponse(new ResponseCondition(422))
                                                .withResponse(
                                                        new ResponseCondition(201, "book created successfully")
                                                                .withHeader(new StringParameterCondition("Location", "the location of the newly created resource"))
                                                                .withBody(
                                                                        new BodyCondition("application/json")
                                                                                .withSchema(fixture("fixtures/raml-model-body-test/book-response-schema.json"))
                                                                                .withExample(fixture("fixtures/raml-model-body-test/book-response-example.json"))
                                                                )
                                                )
                                )
                );
    }

    private String makeError(List<ValidationResult> errors) {
        List<String> lines = newArrayList("RAML validation failed for the following reasons:");
        errors.forEach(r -> lines.add(r.getMessage()));
        return Joiner.on("\n").join(lines);
    }
}