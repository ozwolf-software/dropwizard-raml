package net.ozwolf.raml.validator.testutil;

import net.ozwolf.raml.validator.SpecificationViolation;
import org.assertj.core.api.Condition;

public class SpecificationViolationConditions {
    public static Condition<SpecificationViolation> violationOf(String path, String message) {
        return new Condition<>(
                v -> v.getNode().toString().equals(path) && v.getMessage().equals(message),
                "[" + SpecificationViolation.class.getSimpleName() + "<path=" + path + ", message=" + message + ">]"
        );
    }
}
