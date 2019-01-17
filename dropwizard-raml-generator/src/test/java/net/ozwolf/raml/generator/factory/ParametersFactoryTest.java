package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import org.assertj.core.api.Condition;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.generator.matchers.RamlGeneratorErrorMatchers.errorOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"JavaReflectionMemberAccess", "SameParameterValue"})
public class ParametersFactoryTest {
    @Test
    public void shouldDeriveUriParametersForResource() {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getUriParameters(TestResource1.class, parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("testParameter", "string", "test parameter"));
    }

    @Test
    public void shouldRaiseErrorWhenResourceIsMissingAnnotation() {
        ParametersFactory factory = new ParametersFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource2.class, null, errors::add);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("TestResource2 : /test/{testParameter} : has URI parameters but resource is missing [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation"));
    }

    @Test
    public void shouldRaiseErrorsWhenResourceDescribedParametersDoesNotAlignWithPath() {
        ParametersFactory factory = new ParametersFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource3.class, null, errors::add);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("TestResource3 : /test/{testParameter} : parameter [ testParameter ] has no [ @" + RamlParameter.class.getSimpleName() + " ] definition"));
    }

    @Test
    public void shouldDeriveUriParametersForMethod() {
        ParametersFactory factory = new ParametersFactory();

        Path path = pathOf("/{otherParameter}");

        List<RamlParameterModel> parameters = newArrayList();
        factory.getUriParameters(TestResource1.class, path, parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("otherParameter", "string", "test parameter"));
    }

    @Test
    public void shouldDeriveQueryParametersForMethodUsingDescriber() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getQueryParameters(TestResource1.class.getMethod("getOther", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("test-query", "string", "test query parameter"));
    }

    @Test
    public void shouldDeriveQueryParametersForMethodUsingParameters() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getQueryParameters(TestResource1.class.getMethod("getOtherSub", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("test-query", "string", "test query parameter"));
    }

    @Test
    public void shouldDeriveHeaderParametersForMethodUsingDescriber() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getHeaders(TestResource1.class.getMethod("getOther", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("x-test-header", "string", "test header parameter"));
    }

    @Test
    public void shouldDeriveHeaderParametersForMethodUsingParameters() throws NoSuchMethodException {
        ParametersFactory factory = new ParametersFactory();

        List<RamlParameterModel> parameters = newArrayList();
        factory.getHeaders(TestResource1.class.getMethod("getOtherSub", String.class, String.class), parameters::add, null);

        assertThat(parameters)
                .hasSize(1)
                .areAtLeastOne(parameterOf("x-test-header", "string", "test header parameter"));
    }

    @Test
    public void shouldRaiseErrorWhenMethodIsMissingAnnotation() {
        ParametersFactory factory = new ParametersFactory();

        Path path = pathOf("/{otherParameter}");

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource2.class, path, null, errors::add);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("TestResource2 : /test/{testParameter}/{otherParameter} : has URI parameters but resource is missing [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation"));
    }

    @Test
    public void shouldRaiseErrorsWhenMethodDescribedParametersDoesNotAlignWithPath() {
        ParametersFactory factory = new ParametersFactory();

        Path path = pathOf("/{otherParameter}");

        List<RamlGenerationError> errors = newArrayList();
        factory.getUriParameters(TestResource3.class, path, null, errors::add);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("TestResource3 : /test/{testParameter}/{otherParameter} : parameter [ otherParameter ] has no [ @" + RamlParameter.class.getSimpleName() + " ] definition"));
    }

    private static Condition<RamlParameterModel> parameterOf(String name, String type, String description) {
        return new Condition<>(
                m -> m.getName().equals(name) && m.getType().equals(type) && m.getDescription().equals(description),
                String.format("[%s<name=%s, type=%s, description=%s>]", RamlParameterModel.class.getSimpleName(), name, type, description)
        );
    }

    private static Path pathOf(String value) {
        Path path = mock(Path.class);
        when(path.value()).thenReturn(value);
        return path;
    }

    @RamlResource(displayName = "Test Resource 1", description = "test resource 1")
    @RamlUriParameters({
            @RamlParameter(name = "testParameter", type = "string", description = "test parameter"),
            @RamlParameter(name = "otherParameter", type = "string", description = "test parameter")
    })
    @RamlSubResources(
            @RamlSubResource(path = @Path("/{otherParameter}"))
    )
    @Path("/test/{testParameter}")
    public static class TestResource1 {
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

    @RamlUriParameters({})
    @Path("/test/{testParameter}")
    public static class TestResource3 {
        @Path("/{otherParameter}")
        @GET
        public Response getOther() {
            return Response.ok().build();
        }
    }
}