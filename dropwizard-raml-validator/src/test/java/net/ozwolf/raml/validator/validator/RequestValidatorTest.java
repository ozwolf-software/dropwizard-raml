package net.ozwolf.raml.validator.validator;

import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.validator.ApiRequest;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.validator.testutil.SpecificationViolationConditions.violationOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestValidatorTest {
    private final SecurityValidator securityValidator = mock(SecurityValidator.class);
    private final ParameterValidator parameterValidator = mock(ParameterValidator.class);
    private final BodyValidator bodyValidator = mock(BodyValidator.class);

    @Test
    public void shouldRunValidation() {
        ApiRequest descriptor = mock(ApiRequest.class);

        Node node = new Node("request");

        List<SpecificationViolation> securityViolations = violation(node.createChild("security").createChild("schemes"), "security error");
        when(securityValidator.validate(node.createChild("security"), descriptor)).thenReturn(securityViolations);

        defineParameter(node, "headers", descriptor::getHeaderSpecifications, descriptor::getActualHeader);
        defineParameter(node, "queryParameters", descriptor::getQueryParameterSpecifications, descriptor::getActualQueryParameter);
        defineParameter(node, "uriParameters", descriptor::getUriParameterSpecifications, descriptor::getActualPathParameter);

        RamlBody body = mock(RamlBody.class);
        when(body.getContentType()).thenReturn("application/json");
        byte[] content = "test".getBytes();

        when(descriptor.getBodySpecification()).thenReturn(Optional.of(body));
        when(descriptor.getContent()).thenReturn(Optional.of(content));
        Node bodyNode = node.createChild("body");

        List<SpecificationViolation> bodyViolations = violation(bodyNode.createChild("application/json").createChild("schema"), "body error");
        when(bodyValidator.validate(bodyNode, body, content)).thenReturn(bodyViolations);

        List<SpecificationViolation> result = validator().validate(node, descriptor);

        assertThat(result)
                .hasSize(5)
                .areAtLeastOne(violationOf("request.security.schemes", "security error"))
                .areAtLeastOne(violationOf("request.headers.headersParameter.property", "headers error"))
                .areAtLeastOne(violationOf("request.queryParameters.queryParametersParameter.property", "queryParameters error"))
                .areAtLeastOne(violationOf("request.uriParameters.uriParametersParameter.property", "uriParameters error"))
                .areAtLeastOne(violationOf("request.body.application/json.schema", "body error"));
    }

    @Test
    public void shouldReturnViolationForBodyWhenNoContentFound() {
        ApiRequest descriptor = mock(ApiRequest.class);

        Node node = new Node("request");
        when(descriptor.getSecuritySpecifications()).thenReturn(newArrayList());
        when(descriptor.getHeaderSpecifications()).thenReturn(newArrayList());
        when(descriptor.getQueryParameterSpecifications()).thenReturn(newArrayList());
        when(descriptor.getUriParameterSpecifications()).thenReturn(newArrayList());

        RamlBody body = mock(RamlBody.class);
        when(body.getContentType()).thenReturn("application/json");
        when(descriptor.getBodySpecification()).thenReturn(Optional.of(body));
        when(descriptor.getContent()).thenReturn(Optional.empty());

        List<SpecificationViolation> result = validator().validate(node, descriptor);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("request.body.application/json", "must provide request content"));
    }

    private void defineParameter(Node node, String type, Supplier<List<RamlParameter>> specifications, Function<String, List<String>> values) {
        String parameterName = type + "Parameter";

        RamlParameter specification = mock(RamlParameter.class);
        when(specification.getName()).thenReturn(parameterName);
        when(specifications.get()).thenReturn(newArrayList(specification));

        List<String> actualValues = newArrayList("value1");
        when(values.apply(parameterName)).thenReturn(actualValues);

        Node parameterNode = node.createChild(type).createChild(parameterName);
        List<SpecificationViolation> violations = violation(parameterNode.createChild("property"), type + " error");
        when(parameterValidator.validate(parameterNode, specification, actualValues)).thenReturn(violations);
    }

    private RequestValidator validator() {
        return new RequestValidator(securityValidator, parameterValidator, bodyValidator);
    }

    private static List<SpecificationViolation> violation(Node node, String message) {
        return newArrayList(new SpecificationViolation(node, message));
    }
}