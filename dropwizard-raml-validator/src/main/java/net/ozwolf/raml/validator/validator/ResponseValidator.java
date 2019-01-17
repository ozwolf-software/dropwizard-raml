package net.ozwolf.raml.validator.validator;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlResponse;
import net.ozwolf.raml.validator.ApiResponse;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ResponseValidator {
    private final ParameterValidator parameterValidator;
    private final BodyValidator bodyValidator;

    public ResponseValidator() {
        this.parameterValidator = new ParameterValidator();
        this.bodyValidator = new BodyValidator();
    }

    @VisibleForTesting
    protected ResponseValidator(ParameterValidator parameterValidator, BodyValidator bodyValidator) {
        this.parameterValidator = parameterValidator;
        this.bodyValidator = bodyValidator;
    }

    public List<SpecificationViolation> validate(Node node, ApiResponse descriptor) {
        List<SpecificationViolation> violations = newArrayList();

        Node statusNode = node.createChild(String.valueOf(descriptor.getStatus()));

        RamlResponse responseSpecification = descriptor.getResponseSpecification().orElse(null);

        if (responseSpecification == null) {
            violations.add(new SpecificationViolation(statusNode, "no response defined for status code"));
            return violations;
        }

        Node headersNode = statusNode.createChild("headers");
        for (RamlParameter parameter : responseSpecification.getHeaders()) {
            Node parameterNode = headersNode.createChild(parameter.getName());
            List<String> values = descriptor.getActualHeader(parameter.getName());
            violations.addAll(parameterValidator.validate(parameterNode, parameter, values));
        }

        RamlBody bodySpecification = descriptor.getBodySpecification().orElse(null);
        Node bodyNode = statusNode.createChild("body");
        if (bodySpecification == null) {
            violations.add(new SpecificationViolation(bodyNode.createChild(descriptor.getMediaType().toString()), "no specification for media type"));
            return violations;
        }

        byte[] content = descriptor.getContent().orElse(null);
        if (content == null || content.length <= 0) {
            violations.add(new SpecificationViolation(bodyNode.createChild(descriptor.getMediaType().toString()), "must return content in response"));
        } else {
            violations.addAll(bodyValidator.validate(bodyNode, bodySpecification, content));
        }

        return violations;
    }
}
