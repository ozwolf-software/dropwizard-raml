package net.ozwolf.raml.validator.validator;

import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlSecurity;
import net.ozwolf.raml.validator.ApiRequest;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.validator.testutil.PathConditions.isPathOf;
import static net.ozwolf.raml.validator.testutil.SpecificationViolationConditions.violationOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityValidatorTest {
    private final ParameterValidator parameterValidator = mock(ParameterValidator.class);

    @Test
    public void shouldPassValidation() {
        Node node = new Node("security");

        RamlParameter header = mock(RamlParameter.class);
        RamlSecurity bearerSecurity = bearerAuthorization(header);

        List<String> authorizationValues = newArrayList("Bearer ABC123");

        ApiRequest descriptor = descriptor(true, newArrayList(bearerSecurity), "Authorization", authorizationValues);

        when(parameterValidator.validate(argThat(isPathOf("security.x-bearer.headers.Authorization")), eq(header), eq(authorizationValues))).thenReturn(newArrayList());

        List<SpecificationViolation> result = new SecurityValidator(parameterValidator).validate(node, descriptor);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnNoViolationsIfResourceHasNoSecuritySpecification() {
        Node node = new Node("security");
        ApiRequest descriptor = descriptor(false, newArrayList(), null, newArrayList());

        List<SpecificationViolation> result = new SecurityValidator(parameterValidator).validate(node, descriptor);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnViolationWhenNoSecuritySchemeIsAdheredTo() {
        Node node = new Node("security");

        ApiRequest descriptor = descriptor(true, newArrayList(), "Authorization", newArrayList());

        List<SpecificationViolation> result = new SecurityValidator(parameterValidator).validate(node, descriptor);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("security.schemes", "must provide valid security credentials"));
    }

    @Test
    public void shouldReturnViolationWhenParametersFailValidation() {
        Node node = new Node("security");

        RamlParameter header = mock(RamlParameter.class);
        RamlSecurity bearerSecurity = bearerAuthorization(header);
        List<String> authorizationValues = newArrayList("Bearer ABC123");

        ApiRequest descriptor = descriptor(true, newArrayList(bearerSecurity), "Authorization", authorizationValues);

        List<SpecificationViolation> parameterViolations = newArrayList(
                new SpecificationViolation(Node.build(node, "x-bearer", "headers", "Authorization", "pattern"), "must match pattern [ Bearer (.*) ]")
        );

        when(parameterValidator.validate(argThat(isPathOf("security.x-bearer.headers.Authorization")), eq(header), eq(authorizationValues))).thenReturn(parameterViolations);

        List<SpecificationViolation> result = new SecurityValidator(parameterValidator).validate(node, descriptor);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("security.x-bearer.headers.Authorization.pattern", "must match pattern [ Bearer (.*) ]"));
    }

    private static RamlSecurity bearerAuthorization(RamlParameter parameter) {
        RamlSecurity scheme = mock(RamlSecurity.class);
        when(scheme.getName()).thenReturn("x-bearer");

        when(parameter.getName()).thenReturn("Authorization");

        when(scheme.getHeaders()).thenReturn(newArrayList(parameter));
        when(scheme.getQueryParameters()).thenReturn(newArrayList());

        return scheme;
    }

    private static ApiRequest descriptor(boolean hasSecurity,
                                         List<RamlSecurity> active,
                                         String headerName,
                                         List<String> headerValues) {
        ApiRequest result = mock(ApiRequest.class);
        when(result.hasSecurity()).thenReturn(hasSecurity);
        when(result.getActiveSecurity()).thenReturn(hasSecurity ? active : newArrayList());
        when(result.getActualHeader(headerName)).thenReturn(hasSecurity ? headerValues : newArrayList());
        return result;
    }
}