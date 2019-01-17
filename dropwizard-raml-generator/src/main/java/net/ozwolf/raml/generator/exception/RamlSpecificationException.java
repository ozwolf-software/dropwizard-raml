package net.ozwolf.raml.generator.exception;

import org.raml.v2.api.model.common.ValidationResult;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.joining;

public class RamlSpecificationException extends RuntimeException {
    private final List<ValidationResult> results;

    public RamlSpecificationException(List<ValidationResult> results) {
        super("RAML specification contains errors.");
        this.results = results;
    }

    @Override
    public String getMessage() {
        List<String> errors = newArrayList(super.getMessage());
        results.forEach(r -> errors.add("\t" + r.getPath() + ": " + r.getMessage()));
        return errors.stream().collect(joining("\n"));
    }
}
