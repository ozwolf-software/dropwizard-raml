package net.ozwolf.raml.generator.exception;

public class RamlGenerationUnhandledException extends RuntimeException {
    public RamlGenerationUnhandledException(Throwable cause) {
        super("Unexpected error generating RAML.", cause);
    }
}
