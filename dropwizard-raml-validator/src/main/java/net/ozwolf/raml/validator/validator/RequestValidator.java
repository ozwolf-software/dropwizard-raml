package net.ozwolf.raml.validator.validator;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.validator.ApiRequest;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RequestValidator {
    private final SecurityValidator securityValidator;
    private final ParameterValidator parameterValidator;
    private final BodyValidator bodyValidator;

    public RequestValidator() {
        this.securityValidator = new SecurityValidator();
        this.parameterValidator = new ParameterValidator();
        this.bodyValidator = new BodyValidator();
    }

    @VisibleForTesting
    RequestValidator(SecurityValidator securityValidator, ParameterValidator parameterValidator, BodyValidator bodyValidator) {
        this.securityValidator = securityValidator;
        this.parameterValidator = parameterValidator;
        this.bodyValidator = bodyValidator;
    }

    public List<SpecificationViolation> validate(Node node, ApiRequest descriptor) {
        List<SpecificationViolation> violations = newArrayList();

        violations.addAll(securityValidator.validate(new Node(node, "security"), descriptor));

        Node headersNode = node.createChild("headers");
        Node queryParametersNode = node.createChild("queryParameters");
        Node uriParametersNode = node.createChild("uriParameters");

        for (RamlParameter parameter : descriptor.getHeaderSpecifications()) {
            Node parameterNode = headersNode.createChild(parameter.getName());
            List<String> values = descriptor.getActualHeader(parameter.getName());
            violations.addAll(parameterValidator.validate(parameterNode, parameter, values));
        }

        for (RamlParameter parameter : descriptor.getQueryParameterSpecifications()) {
            Node parameterNode = queryParametersNode.createChild(parameter.getName());
            List<String> values = descriptor.getActualQueryParameter(parameter.getName());
            violations.addAll(parameterValidator.validate(parameterNode, parameter, values));
        }

        for (RamlParameter parameter : descriptor.getUriParameterSpecifications()) {
            Node parameterNode = uriParametersNode.createChild(parameter.getName());
            List<String> values = descriptor.getActualPathParameter(parameter.getName());
            violations.addAll(parameterValidator.validate(parameterNode, parameter, values));
        }

        RamlBody body = descriptor.getBodySpecification().orElse(null);

        if (body != null) {
            Node bodyNode = node.createChild("body");
            byte[] content = descriptor.getContent().orElse(null);
            if (content == null || content.length <= 0) {
                violations.add(new SpecificationViolation(bodyNode.createChild(body.getContentType()), "must provide request content"));
            } else {
                violations.addAll(bodyValidator.validate(bodyNode, body, content));
            }
        }

        return violations;
    }
}
