package net.ozwolf.raml.generator.exception;

import net.ozwolf.raml.generator.RamlGenerator;

import java.util.List;

/**
 * <h1>RAML Generation Exception</h1>
 *
 * The checked exception thrown by the RAML Generator when one or more generation errors are detected during the generation process.
 *
 * @see RamlGenerator#generate()
 */
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
