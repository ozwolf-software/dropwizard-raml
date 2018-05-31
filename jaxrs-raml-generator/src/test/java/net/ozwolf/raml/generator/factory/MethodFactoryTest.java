package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.RamlDescription;
import net.ozwolf.raml.annotations.RamlSecuredBy;
import net.ozwolf.raml.annotations.RamlTraits;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlMethodModel;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import net.ozwolf.raml.generator.model.RamlResponseModel;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"JavaReflectionMemberAccess", "unchecked"})
class MethodFactoryTest {
    private final ParametersFactory parametersFactory = mock(ParametersFactory.class);
    private final ResponseFactory responseFactory = mock(ResponseFactory.class);

    @Test
    void shouldCreateMethodModel() throws NoSuchMethodException {
        Method method = MethodFactoryTest.class.getMethod("testMethod");

        RamlParameterModel queryParameter = mock(RamlParameterModel.class);
        when(queryParameter.getName()).thenReturn("test-query");

        RamlParameterModel header = mock(RamlParameterModel.class);
        when(header.getName()).thenReturn("x-test-header");

        RamlResponseModel response = mock(RamlResponseModel.class);
        when(response.getStatus()).thenReturn(200);

        Answer<Void> queryOnSuccess = onSuccessFor(queryParameter);
        Answer<Void> headerOnSuccess = onSuccessFor(header);
        Answer<Void> responseOnSuccess = onSuccessFor(response);

        doAnswer(queryOnSuccess).when(parametersFactory).getQueryParameters(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(headerOnSuccess).when(parametersFactory).getHeaders(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(responseOnSuccess).when(responseFactory).getResponses(eq(method), any(Consumer.class), any(Consumer.class));

        MethodFactory factory = new MethodFactory();
        factory.setParametersFactory(parametersFactory);
        factory.setResponseFactory(responseFactory);

        AtomicReference<RamlMethodModel> result = new AtomicReference<>();

        factory.getMethod(method, result::set, null);

        assertThat(result.get()).isNotNull();

        RamlMethodModel model = result.get();

        assertThat(model.getAction()).isEqualTo("get");
        assertThat(model.getQueryParameters())
                .hasSize(1)
                .containsEntry("test-query", queryParameter);

        assertThat(model.getHeaders())
                .hasSize(1)
                .containsEntry("x-test-header", header);

        assertThat(model.getResponses())
                .hasSize(1)
                .containsEntry(200, response);
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
        Method method = MethodFactoryTest.class.getMethod("testMethod");

        RamlGenerationError queryError = new RamlGenerationError(MethodFactoryTest.class, method, "query parameter wrong");
        RamlGenerationError headerError = new RamlGenerationError(MethodFactoryTest.class, method, "header wrong");
        RamlGenerationError responseError = new RamlGenerationError(MethodFactoryTest.class, method, "response wrong");

        Answer<Void> queryOnError = onErrorFor(queryError);
        Answer<Void> headerOnError = onErrorFor(headerError);
        Answer<Void> responseOnError = onErrorFor(responseError);

        doAnswer(queryOnError).when(parametersFactory).getQueryParameters(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(headerOnError).when(parametersFactory).getHeaders(eq(method), any(Consumer.class), any(Consumer.class));
        doAnswer(responseOnError).when(responseFactory).getResponses(eq(method), any(Consumer.class), any(Consumer.class));

        MethodFactory factory = new MethodFactory();
        factory.setParametersFactory(parametersFactory);
        factory.setResponseFactory(responseFactory);

        List<RamlGenerationError> errors = newArrayList();
        factory.getMethod(method, null, errors::add);

        assertThat(errors)
                .hasSize(3)
                .contains(queryError, headerError, responseError);
    }

    private static <T> Answer<Void> onSuccessFor(T result){
        return i -> {
            Consumer<T> consumer = i.getArgument(1);
            consumer.accept(result);
            return null;
        };
    }

    private static <T> Answer<Void> onErrorFor(T result){
        return i -> {
            Consumer<T> consumer = i.getArgument(2);
            consumer.accept(result);
            return null;
        };
    }

    @RamlDescription("Test method")
    @RamlSecuredBy("test-token")
    @RamlTraits("has404")
    @GET
    public Response testMethod(){
        return Response.ok().build();
    }

    @HEAD
    public Response head(){
        return Response.ok().build();
    }
}