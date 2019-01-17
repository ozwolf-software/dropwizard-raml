package net.ozwolf.raml.validator.validator;

import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlResponse;
import net.ozwolf.raml.validator.ApiResponse;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.validator.testutil.SpecificationViolationConditions.violationOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseValidatorTest {
    private final ParameterValidator parameterValidator = mock(ParameterValidator.class);
    private final BodyValidator bodyValidator = mock(BodyValidator.class);

    @Test
    public void shouldRunValidation() {
        ApiResponse descriptor = mock(ApiResponse.class);
        when(descriptor.getStatus()).thenReturn(200);
        when(descriptor.getMediaType()).thenReturn(MediaType.APPLICATION_JSON_TYPE);

        Node node = new Node("response");
        Node statusNode = node.createChild("200");

        String headerName = "test-header";
        String headerValue = "value1";
        RamlParameter header = mock(RamlParameter.class);
        when(header.getName()).thenReturn(headerName);

        RamlResponse response = mock(RamlResponse.class);
        when(response.getHeaders()).thenReturn(newArrayList(header));

        when(descriptor.getResponseSpecification()).thenReturn(Optional.of(response));
        when(descriptor.getActualHeader(headerName)).thenReturn(newArrayList(headerValue));

        Node headerNode = statusNode.createChild("headers").createChild(headerName);
        List<SpecificationViolation> headerViolations = newArrayList(
                new SpecificationViolation(headerNode.createChild("error"), "header error")
        );
        when(parameterValidator.validate(headerNode, header, newArrayList(headerValue))).thenReturn(headerViolations);

        RamlBody body = mock(RamlBody.class);
        when(descriptor.getBodySpecification()).thenReturn(Optional.of(body));
        byte[] content = "content".getBytes();
        when(descriptor.getContent()).thenReturn(Optional.of(content));

        Node bodyNode = statusNode.createChild("body");
        List<SpecificationViolation> bodyViolations = newArrayList(
                new SpecificationViolation(bodyNode.createChild("application/json").createChild("error"), "body error")
        );
        when(bodyValidator.validate(bodyNode, body, content)).thenReturn(bodyViolations);

        ResponseValidator validator = new ResponseValidator(parameterValidator, bodyValidator);
        List<SpecificationViolation> result = validator.validate(node, descriptor);

        assertThat(result)
                .hasSize(2)
                .areAtLeastOne(violationOf("response.200.headers.test-header.error", "header error"))
                .areAtLeastOne(violationOf("response.200.body.application/json.error", "body error"));
    }

    @Test
    public void shouldReturnViolationWhenNoResponseDefinedForStatusCode() {
        ApiResponse descriptor = mock(ApiResponse.class);
        when(descriptor.getStatus()).thenReturn(200);

        Node node = new Node("response");

        when(descriptor.getResponseSpecification()).thenReturn(Optional.empty());

        ResponseValidator validator = new ResponseValidator(parameterValidator, bodyValidator);
        List<SpecificationViolation> result = validator.validate(node, descriptor);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("response.200", "no response defined for status code"));
    }

    @Test
    public void shouldReturnMissingBodyDefinitionForContentTypeViolation() {
        ApiResponse descriptor = mock(ApiResponse.class);
        when(descriptor.getStatus()).thenReturn(200);
        when(descriptor.getMediaType()).thenReturn(MediaType.APPLICATION_JSON_TYPE);

        Node node = new Node("response");

        RamlResponse response = mock(RamlResponse.class);
        when(response.getHeaders()).thenReturn(newArrayList());
        when(descriptor.getResponseSpecification()).thenReturn(Optional.of(response));

        when(descriptor.getBodySpecification()).thenReturn(Optional.empty());

        ResponseValidator validator = new ResponseValidator(parameterValidator, bodyValidator);
        List<SpecificationViolation> result = validator.validate(node, descriptor);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("response.200.body.application/json", "no specification for media type"));
    }

    @Test
    public void shouldReturnMissingContentViolation() {
        ApiResponse descriptor = mock(ApiResponse.class);
        when(descriptor.getStatus()).thenReturn(200);
        when(descriptor.getMediaType()).thenReturn(MediaType.APPLICATION_JSON_TYPE);

        Node node = new Node("response");

        RamlResponse response = mock(RamlResponse.class);
        when(response.getHeaders()).thenReturn(newArrayList());
        when(descriptor.getResponseSpecification()).thenReturn(Optional.of(response));

        RamlBody body = mock(RamlBody.class);
        when(descriptor.getBodySpecification()).thenReturn(Optional.of(body));
        when(descriptor.getContent()).thenReturn(Optional.empty());

        ResponseValidator validator = new ResponseValidator(parameterValidator, bodyValidator);
        List<SpecificationViolation> result = validator.validate(node, descriptor);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("response.200.body.application/json", "must return content in response"));
    }
}