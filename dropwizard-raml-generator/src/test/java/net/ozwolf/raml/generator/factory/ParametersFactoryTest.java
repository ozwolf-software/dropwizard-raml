package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.generator.matchers.RamlGeneratorErrorMatchers.errorOf;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("JavaReflectionMemberAccess")
class ParametersFactoryTest {
    @Test
    void shouldDeriveUriParametersForResource() {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getUriParameters(TestResource1.class, parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("testParameter", "string", "test parameter"));
    }

    @Test
    void shouldRaiseErrorWhenResourceIsMissingAnnotation() {
        ParametersFactory factory = new ParametersFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource2.class, null, errors::add);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("TestResource2 : has URI parameters but is missing [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation"));
    }

    @Test
    void shouldRaiseErrorsWhenResourceDescribedParametersDoesNotAlignWithPath() {
        ParametersFactory factory = new ParametersFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource3.class, null, errors::add);

        assertThat(errors)
                .hasSize(2)
                .areAtLeastOne(errorOf("TestResource3 : parameter [ testParameter ] has no [ @" + RamlParameter.class.getSimpleName() + " ] definition"))
                .areAtLeastOne(errorOf("TestResource3 : parameter [ unknownParameter ] described but is not in path"));
    }

    @Test
    void shouldDeriveUriParametersForMethod() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getUriParameters(TestResource1.class.getMethod("getOther", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("otherParameter", "string", "test parameter"));
    }

    @Test
    void shouldDeriveQueryParametersForMethodUsingDescriber() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getQueryParameters(TestResource1.class.getMethod("getOther", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("test-query", "string", "test query parameter"));
    }

    @Test
    void shouldDeriveQueryParametersForMethodUsingParameters() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getQueryParameters(TestResource1.class.getMethod("getOtherSub", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("test-query", "string", "test query parameter"));
    }

    @Test
    void shouldDeriveHeaderParametersForMethodUsingDescriber() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getHeaders(TestResource1.class.getMethod("getOther", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("x-test-header", "string", "test header parameter"));
    }

    @Test
    void shouldDeriveHeaderParametersForMethodUsingParameters() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getHeaders(TestResource1.class.getMethod("getOtherSub", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("x-test-header", "string", "test header parameter"));
    }

    @Test
    void shouldRaiseErrorWhenMethodIsMissingAnnotation() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource2.class.getMethod("getOther"), null, errors::add);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("TestResource2.getOther : has URI parameters but is missing [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation"));
    }

    @Test
    void shouldRaiseErrorsWhenMethodDescribedParametersDoesNotAlignWithPath() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource3.class.getMethod("getOther"), null, errors::add);

        assertThat(errors)
                .hasSize(2)
                .areAtLeastOne(errorOf("TestResource3.getOther : parameter [ otherParameter ] has no [ @" + RamlParameter.class.getSimpleName() + " ] definition"))
                .areAtLeastOne(errorOf("TestResource3.getOther : parameter [ unknownParameter ] described but is not in path"));
    }

    private static Condition<RamlParameterModel> parameterOf(String name, String type, String description) {
        return new Condition<>(
                m -> m.getName().equals(name) && m.getType().equals(type) && m.getDescription().equals(description),
                String.format("[%s<name=%s, type=%s, description=%s>]", RamlParameterModel.class.getSimpleName(), name, type, description)
        );
    }

    @RamlResource(displayName = "Test Resource 1", description = "test resource 1")
    @RamlUriParameters(
            @RamlParameter(name = "testParameter", type = "string", description = "test parameter")
    )
    @Path("/test/{testParameter}")
    public static class TestResource1 {
        @RamlUriParameters(
                @RamlParameter(name = "otherParameter", type = "string", description = "test parameter")
        )
        @RamlQueryParameters(
                @RamlParameter(name = "test-query", type = "string", description = "test query parameter")
        )
        @RamlHeaders(
                @RamlParameter(name = "x-test-header", type = "string", description = "test header parameter")
        )
        @Path("/{otherParameter}")
        @GET
        public Response getOther(@QueryParam("test-query") String testQuery,
                                 @HeaderParam("x-test-header") String testHeader) {
            return Response.ok().build();
        }

        @Path("/{otherParameter}/sub")
        @GET
        public Response getOtherSub(@QueryParam("test-query") @RamlDescription("test query parameter") String testQuery,
                                    @HeaderParam("x-test-header") @RamlDescription("test header parameter") String testHeader) {
            return Response.ok().build();
        }
    }

    @Path("/test/{testParameter}")
    public static class TestResource2 {
        @Path("/{otherParameter}")
        @GET
        public Response getOther() {
            return Response.ok().build();
        }
    }

    @RamlUriParameters(
            @RamlParameter(name = "unknownParameter", type = "string", description = "an unknown parameter")
    )
    @Path("/test/{testParameter}")
    public static class TestResource3 {
        @RamlUriParameters(
                @RamlParameter(name = "unknownParameter", type = "string", description = "an unknown parameter")
        )
        @Path("/{otherParameter}")
        @GET
        public Response getOther() {
            return Response.ok().build();
        }
    }
}