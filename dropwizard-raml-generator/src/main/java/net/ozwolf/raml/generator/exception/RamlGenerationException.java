package net.ozwolf.raml.generator.exception;

import java.util.List;

public class RamlGenerationException extends Exception {
    private final List<RamlGenerationError> errors;

    public RamlGenerationException(List<RamlGenerationError> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder("RAML generation encountered errors:");
        for (RamlGenerationError error : errors) {
            builder = builder.append("\n\t").append(error.getMessage());
        }

        return builder.toString();
    }
}
