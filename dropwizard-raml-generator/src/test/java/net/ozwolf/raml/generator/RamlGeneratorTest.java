package net.ozwolf.raml.generator;

import com.google.common.base.Joiner;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class RamlGeneratorTest {
    @AfterEach
    void tearDown() {
        RamlMedia.instance().reset();
    }

    @Test
    void shouldGenerateValidRAML() throws RamlGenerationException {
        RamlGenerator generator = new RamlGenerator("net.ozwolf.raml.example", "1.0.0");

        String raml = generator.generate();

        System.out.println(raml);

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


}