package net.ozwolf.raml.generator.matchers;

import net.ozwolf.raml.generator.exception.RamlGenerationError;
import org.assertj.core.api.Condition;

public class RamlGeneratorErrorMatchers {
    public static Condition<RamlGenerationError> errorOf(String message) {
        return new Condition<>(
                e -> e.getMessage().equals(message),
                String.format("[%s<message=%s>]", RamlGenerationError.class.getSimpleName(), message)
        );
    }
}
