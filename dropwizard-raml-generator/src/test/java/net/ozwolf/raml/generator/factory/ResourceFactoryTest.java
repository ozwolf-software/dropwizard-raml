package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlMethodModel;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import net.ozwolf.raml.generator.model.RamlResourceModel;
import net.ozwolf.raml.generator.model.RamlResponseModel;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static jersey.repackaged.com.google.common.collect.Maps.newHashMap;
import static net.ozwolf.raml.generator.matchers.RamlGeneratorErrorMatchers.errorOf;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("SameParameterValue")
public class ResourceFactoryTest {
    private final ParametersFactory parametersFactory = mock(ParametersFactory.class);
    private final MethodFactory methodFactory = mock(MethodFactory.class);

    private final static Map<Integer, RamlResponseModel> GLOBAL_RESPONSES = newHashMap();

    @Test
    public void shouldGenerateResourceModelIncludingSubResources() throws NoSuchMethodException {
        Method getBase = CorrectResource.class.getMethod("getBase");
        Method postSub = CorrectResource.class.getMethod("postSub");
        Method getSub = CorrectResource.class.getMethod("getSub");
        Method postSubSub = CorrectResource.class.getMethod("postSubSub");
        Method getSubSub = CorrectResource.class.getMethod("getSubSub");

        RamlMethodModel getBaseModel = methodOnSuccess("get", getBase);
        RamlMethodModel postSubModel = methodOnSuccess("post", postSub);
        RamlMethodModel getSubModel = methodOnSuccess("get", getSub);
        RamlMethodModel postSubSubModel = methodOnSuccess("post", postSubSub);
        RamlMethodModel getSubSubModel = methodOnSuccess("get", getSubSub);

        RamlParameterModel idModel = uriParameterOnSuccess(CorrectResource.class, "/sub-resource/{id}", "id");

        ResourceFactory factory = new ResourceFactory();
        factory.setMethodFactory(methodFactory);
        factory.setParametersFactory(parametersFactory);

        AtomicReference<RamlResourceModel> result = new AtomicReference<>();
        factory.getResource(CorrectResource.class, GLOBAL_RESPONSES, result::set, null);

        RamlResourceModel model = result.get();
        assertThat(model).isNotNull();
        assertThat(model.getMethods())
                .hasSize(1)
                .containsEntry("get", getBaseModel);

        assertThat(model.getResources())
                .hasSize(2)
                .containsKeys("/sub-resource", "/sub-resource/{id}");

        RamlResourceModel subResource = model.getResources().get("/sub-resource");

        assertThat(subResource.getMethods())
                .hasSize(2)
                .containsEntry("post", postSubModel)
                .containsEntry("get", getSubModel);

        RamlResourceModel subSubResource = model.getResources().get("/sub-resource/{id}");

        assertThat(subSubResource.getMethods())
                .hasSize(2)
                .containsEntry("post", postSubSubModel)
                .containsEntry("get", getSubSubModel);

        assertThat(subSubResource.getUriParameters())
                .hasSize(1)
                .containsEntry("id", idModel);
    }

    @Test
    public void shouldRaiseErrorThatResourceIsMissingRamlResourceAnnotation() {
        ResourceFactory factory = new ResourceFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getResource(MissingAnnotationResource.class, GLOBAL_RESPONSES, null, errors::add);

        assertThat(errors)
                .hasSize(2)
                .areAtLeastOne(errorOf("MissingAnnotationResource : missing [ @" + Path.class.getSimpleName() + " ] annotation"))
                .areAtLeastOne(errorOf("MissingAnnotationResource : missing [ @" + RamlResource.class.getSimpleName() + " ] annotation"));
    }

    @Test
    public void shouldRaiseErrorsCollectedFromProcessAndNotTriggerOnSuccess() throws NoSuchMethodException {
        Method getBase = CorrectResource.class.getMethod("getBase");
        Method postSub = CorrectResource.class.getMethod("postSub");
        Method getSub = CorrectResource.class.getMethod("getSub");
        Method postSubSub = CorrectResource.class.getMethod("postSubSub");
        Method getSubSub = CorrectResource.class.getMethod("getSubSub");

        RamlGenerationError getBaseError = methodOnError(CorrectResource.class, getBase, "get base error");
        RamlGenerationError postSubError = methodOnError(CorrectResource.class, postSub, "post sub error");
        RamlGenerationError getSubError = methodOnError(CorrectResource.class, getSub, "get sub error");
        RamlGenerationError postSubSubError = methodOnError(CorrectResource.class, postSubSub, "post sub sub error");
        RamlGenerationError getSubSubError = methodOnError(CorrectResource.class, getSubSub, "get sub sub error");

        RamlGenerationError idError = uriParameterOnError(CorrectResource.class, "/sub-resource/{id}", "id parameter error");

        ResourceFactory factory = new ResourceFactory();
        factory.setParametersFactory(parametersFactory);
        factory.setMethodFactory(methodFactory);

        List<RamlGenerationError> errors = newArrayList();

        factory.getResource(CorrectResource.class, GLOBAL_RESPONSES, null, errors::add);

        assertThat(errors)
                .hasSize(6)
                .contains(
                        getBaseError,
                        postSubError,
                        getSubError,
                        postSubSubError,
                        getSubSubError,
                        idError
                );
    }

    private RamlMethodModel methodOnSuccess(String action, Method method) {
        RamlMethodModel model = mock(RamlMethodModel.class);
        when(model.getAction()).thenReturn(action);

        Answer<Void> answer = i -> {
            Consumer<RamlMethodModel> onSuccess = i.getArgument(2);
            onSuccess.accept(model);
            return null;
        };

        doAnswer(answer).when(methodFactory).getMethod(eq(method), eq(GLOBAL_RESPONSES), any(), any());
        return model;
    }

    private RamlGenerationError methodOnError(Class<?> resource, Method method, String message) {
        RamlGenerationError error = new RamlGenerationError(resource, method, message);
        Answer<Void> answer = i -> {
            Consumer<RamlGenerationError> onError = i.getArgument(3);
            onError.accept(error);
            return null;
        };

        doAnswer(answer).when(methodFactory).getMethod(eq(method), eq(GLOBAL_RESPONSES), any(), any());
        return error;
    }

    private RamlParameterModel uriParameterOnSuccess(Class<?> resource, String path, String name) {
        RamlParameterModel model = mock(RamlParameterModel.class);
        when(model.getName()).thenReturn(name);

        Answer<Void> answer = i -> {
            Consumer<RamlParameterModel> onSuccess = i.getArgument(2);
            onSuccess.accept(model);
            return null;
        };

        doAnswer(answer).when(parametersFactory).getUriParameters(eq(resource), argThat(a -> a.value().equals(path)), any(), any());
        return model;
    }

    private RamlGenerationError uriParameterOnError(Class<?> resource, String path, String message) {
        RamlGenerationError error = new RamlGenerationError(resource, message);

        Answer<Void> answer = i -> {
            Consumer<RamlGenerationError> onError = i.getArgument(3);
            onError.accept(error);
            return null;
        };

        doAnswer(answer).when(parametersFactory).getUriParameters(eq(resource), argThat(a -> a.value().equals(path)), any(), any());
        return error;
    }

    @RamlResource(displayName = "Correct", description = "a correct resource")
    @RamlUriParameters(@RamlParameter(name = "id", type = "string", description = "the sub-resource id"))
    @RamlSubResources({
            @RamlSubResource(path = @Path("/sub-resource"), description = "a sub resource"),
            @RamlSubResource(path = @Path("/sub-resource/{id}"))
    })
    @Path("/correct")
    public class CorrectResource {
        @GET
        public Response getBase() {
            return Response.ok().build();
        }

        @Path("/sub-resource")
        @POST
        public Response postSub() {
            return Response.ok().build();
        }

        @Path("/sub-resource")
        @GET
        public Response getSub() {
            return Response.ok().build();
        }

        @Path("/sub-resource/{id}")
        @PUT
        public Response postSubSub() {
            return Response.ok().build();
        }

        @Path("/sub-resource/{id}")
        @GET
        public Response getSubSub() {
            return Response.ok().build();
        }
    }

    private class MissingAnnotationResource {
    }
}