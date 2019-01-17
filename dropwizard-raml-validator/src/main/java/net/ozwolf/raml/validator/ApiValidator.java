package net.ozwolf.raml.validator;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.validator.validator.RequestValidator;
import net.ozwolf.raml.validator.validator.ResponseValidator;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ApiValidator {
    private final RequestValidator requestValidator;
    private final ResponseValidator responseValidator;

    public ApiValidator() {
        this.requestValidator = new RequestValidator();
        this.responseValidator = new ResponseValidator();
    }

    @VisibleForTesting
    protected ApiValidator(RequestValidator requestValidator, ResponseValidator responseValidator) {
        this.requestValidator = requestValidator;
        this.responseValidator = responseValidator;
    }

    public List<SpecificationViolation> validate(Node node, ApiRequest request, ApiResponse response) {
        List<SpecificationViolation> violations = newArrayList();
        if (request != null && !response.isClientError())
            violations.addAll(requestValidator.validate(node.createChild("request"), request));

        if (response != null)
            violations.addAll(responseValidator.validate(node.createChild("response"), response));
        return violations;
    }
}
