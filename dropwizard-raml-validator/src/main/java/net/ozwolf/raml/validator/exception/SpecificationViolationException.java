package net.ozwolf.raml.validator.exception;

import net.ozwolf.raml.validator.SpecificationViolation;
import net.ozwolf.raml.validator.util.PrintUtils;

import java.util.List;

public class SpecificationViolationException extends RuntimeException {
    private final List<SpecificationViolation> violations;

    public SpecificationViolationException(List<SpecificationViolation> violations) {
        super("Request failed specification validation");
        this.violations = violations;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "\n" + getPrintedViolationsMessage();
    }

    public List<SpecificationViolation> getViolations() {
        return violations;
    }

    public String getPrintedViolationsMessage() {
        return PrintUtils.prettyPrint(violations);
    }
}
