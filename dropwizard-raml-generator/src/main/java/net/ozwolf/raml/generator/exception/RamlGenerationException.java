package net.ozwolf.raml.generator.exception;

public class RamlGenerationException extends RuntimeException {
    public RamlGenerationException(Throwable cause) {
        super("Unexpected error generating RAML.", cause);
    }
}
