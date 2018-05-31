package net.ozwolf.raml.generator.exception;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <h1>RAML Generation Error</h1>
 *
 * An error object relating to a resource.  Used by the generator and factories to build a list of errors while attempting to generate a RAML spec.
 */
public class RamlGenerationError {
    private final Class<?> resource;
    private final Method method;
    private final String message;

    public RamlGenerationError(Class<?> resource, Method method, String message) {
        this.resource = resource;
        this.method = method;
        this.message = message;
    }

    public RamlGenerationError(Class<?> resource, String message) {
        this.resource = resource;
        this.method = null;
        this.message = message;
    }

    public Class<?> getResource() {
        return resource;
    }

    public Method getMethod() {
        return method;
    }

    public String getMessage() {
        String prefix = resource.getSimpleName();
        if (method != null)
            prefix += "." + method.getName();

        return prefix + " : " + message;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "<message=" + getMessage() + ">]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RamlGenerationError))
            return false;

        RamlGenerationError e = (RamlGenerationError) o;

        return Objects.equals(e.resource, this.resource) &&
                Objects.equals(e.method, this.method) &&
                Objects.equals(e.message, this.message);
    }
}
