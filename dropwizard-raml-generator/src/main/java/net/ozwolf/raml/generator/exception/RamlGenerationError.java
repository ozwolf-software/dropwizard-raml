package net.ozwolf.raml.generator.exception;

import java.lang.reflect.Method;

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
}
