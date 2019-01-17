package net.ozwolf.raml.validator.validator;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlSecurity;
import net.ozwolf.raml.validator.ApiRequest;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SecurityValidator {
    private final ParameterValidator parameterValidator;

    SecurityValidator() {
        this.parameterValidator = new ParameterValidator();
    }

    @VisibleForTesting
    SecurityValidator(ParameterValidator parameterValidator) {
        this.parameterValidator = parameterValidator;
    }

    public List<SpecificationViolation> validate(Node node, ApiRequest descriptor) {
        List<SpecificationViolation> violations = newArrayList();

        if (!descriptor.hasSecurity())
            return violations;

        List<RamlSecurity> activeSecurity = descriptor.getActiveSecurity();
        if (activeSecurity.isEmpty()) {
            violations.add(new SpecificationViolation(node.createChild("schemes"), "must provide valid security credentials"));
            return violations;
        }

        for (RamlSecurity scheme : activeSecurity) {
            List<SpecificationViolation> schemeViolations = validateScheme(node.createChild(scheme.getName()), scheme, descriptor);
            if (schemeViolations.isEmpty())
                return newArrayList(); // We found an active scheme and it passed validation
            violations.addAll(schemeViolations);
        }

        return violations;
    }

    private List<SpecificationViolation> validateScheme(Node node, RamlSecurity scheme, ApiRequest descriptor) {
        List<SpecificationViolation> violations = newArrayList();

        Node headersNode = node.createChild("headers");
        Node queryParametersNode = node.createChild("queryParameters");

        for (RamlParameter parameter : scheme.getHeaders()) {
            Node parameterNode = headersNode.createChild(parameter.getName());
            List<String> values = descriptor.getActualHeader(parameter.getName());
            violations.addAll(parameterValidator.validate(parameterNode, parameter, values));
        }

        for (RamlParameter parameter : scheme.getQueryParameters()) {
            Node parameterNode = queryParametersNode.createChild(parameter.getName());
            List<String> values = descriptor.getActualQueryParameter(parameter.getName());
            violations.addAll(parameterValidator.validate(parameterNode, parameter, values));
        }

        return violations;
    }
}
