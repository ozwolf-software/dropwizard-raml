package net.ozwolf.raml.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Joiner;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import org.junit.After;
import org.junit.Test;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class RamlGeneratorITCase {
    private final static ObjectMapper MAPPER = new YAMLMapper();
    @After
    public void tearDown() {
        RamlMedia.instance().reset();
    }

    @Test
    public void shouldGenerateValidRAML() throws RamlGenerationException, IOException {
        RamlGenerator generator = new RamlGenerator("net.ozwolf.raml.example", "1.2.3");

        String raml = generator.generate();

        Map<String, Object> expected = MAPPER.readValue(fixture("fixtures/expected-raml-output.yml"), new Raml());
        Map<String, Object> actual = MAPPER.readValue(raml, new Raml());

        assertThat(expected).isEqualToComparingFieldByField(actual);

        RamlModelResult result = new RamlModelBuilder().buildApi(raml, "/");

        List<ValidationResult> errors = result.getValidationResults();

        assertThat(errors)
                .describedAs(makeError(errors))
                .isEmpty();
    }

    private String makeError(List<ValidationResult> errors) {
        List<String> lines = newArrayList("RAML validation failed for the following reasons:");
        errors.forEach(r -> lines.add(r.getMessage()));
        return Joiner.on("\n").join(lines);
    }

    private static class Raml extends TypeReference<Map<String, Object>>{
    }
}