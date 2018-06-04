package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlBodyModel;
import net.ozwolf.raml.generator.model.RamlMethodModel;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import net.ozwolf.raml.generator.model.RamlResponseModel;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.generator.matchers.RamlGeneratorErrorMatchers.errorOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings({"JavaReflectionMemberAccess", "unchecked"})
class MethodFactoryTest {
    private final ParametersFactory parametersFactory = mock(ParametersFactory.class);
    private final ResponseFactory responseFactory = mock(ResponseFactory.class);
    private final RequestFactory requestFactory = mock(RequestFactory.class);

    @Test
    void shouldCreateMethodModel() throws NoSuchMethodException {
        Method method = MethodFactoryTest.class.getMethod("testMethod", String.class);

        RamlParameterModel queryParameter = mock(RamlParameterModel.class);
        when(queryParameter.getName()).thenReturn("test-query");

        RamlParameterModel header = mock(RamlParameterModel.class);
        when(header.getName()).thenReturn("x-test-header");

        RamlParameterModel formParameter = mock(RamlParameterModel.class);
        when(formParameter.getName()).thenReturn("test-form");

        RamlResponseModel response = mock(RamlResponseModel.class);
        when(response.getStatus()).thenReturn(200);

        RamlBodyModel request = mock(RamlBodyModel.class);
        when(request.getContentType()).thenReturn("application/json");

        Answer<Void> queryOnSuccess = onSuccessFor(queryParameter);
        Answer<Void> headerOnSuccess = onSuccessFor(header);
        Answer<Void> formOnSuccess = onSuccessFor(formParameter);
        Answer<Void> responseOnSuccess = onSuccessFor(response);
        Answer<Void> requestOnSuccess = onSuccessFor(request);

        doAnswer(queryOnSuccess).when(parametersFactory).getQueryParameters(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(headerOnSuccess).when(parametersFactory).getHeaders(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(formOnSuccess).when(parametersFactory).getFormParameters(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(responseOnSuccess).when(responseFactory).getResponses(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(requestOnSuccess).when(requestFactory).getRequests(eq(method), any(), any());

        MethodFactory factory = new MethodFactory();
        factory.setParametersFactory(parametersFactory);
        factory.setResponseFactory(responseFactory);
        factory.setRequestFactory(requestFactory);

        AtomicReference<RamlMethodModel> result = new AtomicReference<>();

        factory.getMethod(method, result::set, null);

        assertThat(result.get()).isNotNull();

        RamlMethodModel model = result.get();

        assertThat(model.getAction()).isEqualTo("put");
        assertThat(model.getQueryParameters())
                .hasSize(1)
                .containsEntry("test-query", queryParameter);

        assertThat(model.getHeaders())
                .hasSize(1)
                .containsEntry("x-test-header", header);

        assertThat(model.getFormParameters())
                .hasSize(1)
                .containsEntry("test-form", formParameter);

        assertThat(model.getResponses())
                .hasSize(1)
                .containsEntry(200, response);

        assertThat(model.getRequests())
                .hasSize(1)
                .containsEntry("application/json", request);
    }

    @Test
    void shouldRaiseErrorWhenUnknownMethod() throws NoSuchMethodException {
        Method method = MethodFactoryTest.class.getMethod("head");

        List<RamlGenerationError> errors = newArrayList();
        Consumer<RamlGenerationError> onError = errors::add;

        MethodFactory factory = new MethodFactory();
        factory.getMethod(method, null, onError);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("MethodFactoryTest.head : unsupported action"));
    }

    @Test
    void shouldCaptureErrorsFromChildFactories() throws NoSuchMethodException {
        Method method = MethodFactoryTest.class.getMethod("testMethod", String.class);

        RamlGenerationError queryError = new RamlGenerationError(MethodFactoryTest.class, method, "query parameter wrong");
        RamlGenerationError headerError = new RamlGenerationError(MethodFactoryTest.class, method, "header wrong");
        RamlGenerationError formError = new RamlGenerationError(MethodFactoryTest.class, method, "form parameter wrong");
        RamlGenerationError responseError = new RamlGenerationError(MethodFactoryTest.class, method, "response wrong");
        RamlGenerationError requestError = new RamlGenerationError(MethodFactoryTest.class, method, "request wrong");

        Answer<Void> queryOnError = onErrorFor(queryError);
        Answer<Void> headerOnError = onErrorFor(headerError);
        Answer<Void> formOnError = onErrorFor(formError);
        Answer<Void> responseOnError = onErrorFor(responseError);
        Answer<Void> requestOnError = onErrorFor(requestError);

        doAnswer(queryOnError).when(parametersFactory).getQueryParameters(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(headerOnError).when(parametersFactory).getHeaders(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(formOnError).when(parametersFactory).getFormParameters(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(responseOnError).when(responseFactory).getResponses(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(requestOnError).when(requestFactory).getRequests(eq(method), any(), any());

        MethodFactory factory = new MethodFactory();
        factory.setParametersFactory(parametersFactory);
        factory.setResponseFactory(responseFactory);
        factory.setRequestFactory(requestFactory);

        List<RamlGenerationError> errors = newArrayList();
        factory.getMethod(method, null, errors::add);

        assertThat(errors)
                .hasSize(5)
                .contains(queryError, headerError, formError, responseError, requestError);
    }

    private static <T> Answer<Void> onSuccessFor(T result) {
        return i -> {
            Consumer<T> consumer = i.getArgument(1);
            consumer.accept(result);
            return null;
        };
    }

    private static <T> Answer<Void> onErrorFor(T result) {
        return i -> {
            Consumer<T> consumer = i.getArgument(2);
            consumer.accept(result);
            return null;
        };
    }

    @RamlDescription("Test method")
    @RamlSecuredBy("test-token")
    @RamlIs("has404")
    @RamlRequests(
            @RamlBody(contentType = "text/plain", example = "example request")
    )
    @PUT
    public Response testMethod(String request) {
        return Response.ok().build();
    }

    @HEAD
    public Response head() {
        return Response.ok().build();
    }
}