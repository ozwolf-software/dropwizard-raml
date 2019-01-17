package net.ozwolf.raml.validator.media;

import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;

import java.util.List;

public interface MediaValidator {
    List<SpecificationViolation> validate(Node node, String schema, byte[] content);
}
