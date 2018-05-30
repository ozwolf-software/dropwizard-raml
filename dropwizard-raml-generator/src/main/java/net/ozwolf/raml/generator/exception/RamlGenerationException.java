package net.ozwolf.raml.generator.exception;

import java.lang.reflect.Method;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RamlGenerationException extends Exception {
    private final List<RamlGenerationError> errors;

    public RamlGenerationException(List<RamlGenerationError> errors) {
        this.errors = errors;
    }

    public RamlGenerationException() {
        this.errors = newArrayList();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public RamlGenerationException record(Class<?> resource, Method method, String message) {
        this.errors.add(new RamlGenerationError(resource, method, message));
        return this;
    }

    public RamlGenerationException record(Class<?> resource, String message) {
        return this.record(resource, null, message);
    }

    public RamlGenerationException record(RamlGenerationException e){
        this.errors.addAll(e.errors);
        return this;
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
