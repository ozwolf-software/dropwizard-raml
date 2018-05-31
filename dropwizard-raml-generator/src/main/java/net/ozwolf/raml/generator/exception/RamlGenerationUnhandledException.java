package net.ozwolf.raml.generator.exception;

/**
 * <h1>RAML Generation Unhandled Exception</h1>
 *
 * An unchecked exception thrown by the gneeration process when an unexpected error occurred during the process, such as serialization, etc.
 */
public class RamlGenerationUnhandledException extends RuntimeException {
    public RamlGenerationUnhandledException(Throwable cause) {
        super("Unexpected error generating RAML.", cause);
    }
}
