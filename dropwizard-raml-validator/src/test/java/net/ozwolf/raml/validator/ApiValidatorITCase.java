package net.ozwolf.raml.validator;

import net.ozwolf.raml.common.model.RamlApplication;
import net.ozwolf.raml.common.model.RamlMethod;
import net.ozwolf.raml.common.model.RamlResource;
import net.ozwolf.raml.common.model.v10.V10_RamlApplication;
import net.ozwolf.raml.validator.exception.SpecificationViolationException;
import org.junit.Test;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiValidatorITCase {
    @Test
    public void shouldRunValidation() {
        Node node = new Node("PUT /authors (application/json) 201");

        RamlApplication application = application();
        RamlResource resource = application.find("/authors", null).orElseThrow(() -> new IllegalStateException("No resource for [ /authors ] defined."));
        RamlMethod method = resource.find("POST").orElseThrow(() -> new IllegalStateException("No POST method defined."));

        List<SpecificationViolation> violations = new ApiValidator().validate(node, request(resource, method), response(method));

        assertThat(violations).isNotEmpty();

        SpecificationViolationException e = new SpecificationViolationException(violations);

        String expected = fixture("fixtures/violations-pretty-print.txt").replaceAll("\r\n", "\n");
        String actual = e.getPrintedViolationsMessage();
        assertThat(actual).isEqualTo(expected);
    }

    private static ApiRequest request(RamlResource resource, RamlMethod method) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.putSingle(HttpHeaders.AUTHORIZATION, "Wrong something");

        byte[] content = fixture("fixtures/requests/invalid-author-request.json").getBytes();

        return new ApiRequest.Builder(resource, method, "/path/to/entity", MediaType.APPLICATION_JSON_TYPE)
                .withHeaders(headers)
                .withContent(content)
                .build();
    }

    private static ApiResponse response(RamlMethod method) {
        byte[] content = fixture("fixtures/responses/author-response.json").getBytes();
        return new ApiResponse.Builder(method, 201, MediaType.APPLICATION_JSON_TYPE)
                .withContent(content)
                .build();
    }

    private static RamlApplication application() {
        RamlModelResult result = new RamlModelBuilder().buildApi("fixtures/apispecs/test-service.yml");
        if (result.hasErrors())
            throw new IllegalStateException("Errors in RAML specification.");

        return new V10_RamlApplication(result.getApiV10());
    }
}