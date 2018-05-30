package net.ozwolf.raml.generator.factory;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.annotations.RamlDescription;
import net.ozwolf.raml.annotations.RamlSecuredBy;
import net.ozwolf.raml.annotations.RamlTraits;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlAction;
import net.ozwolf.raml.generator.model.RamlMethodModel;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import net.ozwolf.raml.generator.model.RamlResponseModel;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class MethodFactory {
    private ParametersFactory parametersFactory;
    private ResponseFactory responseFactory;

    public MethodFactory() {
        this.parametersFactory = new ParametersFactory();
        this.responseFactory = new ResponseFactory();
    }

    public void getMethod(Method method, Consumer<RamlMethodModel> onSuccess, Consumer<RamlGenerationError> onError) {
        String action = RamlAction.getActionFor(method).orElse(null);
        if (action == null) {
            onError.accept(new RamlGenerationError(method.getDeclaringClass(), method, "unsupported action"));
            return;
        }

        CheckedOnError e = new CheckedOnError(onError);

        Map<String, RamlParameterModel> queryParameters = newHashMap();
        parametersFactory.getQueryParameters(method, p -> queryParameters.put(p.getName(), p), e);

        Map<String, RamlParameterModel> headers = newHashMap();
        parametersFactory.getHeaders(method, p -> headers.put(p.getName(), p), e);

        Map<Integer, RamlResponseModel> responses = newHashMap();
        responseFactory.getResponses(method, m -> responses.put(m.getStatus(), m), e);

        if (!e.isInError())
            onSuccess.accept(
                    new RamlMethodModel(
                            action,
                            Optional.ofNullable(method.getAnnotation(RamlDescription.class)).map(RamlDescription::value).orElse(null),
                            Optional.ofNullable(method.getAnnotation(RamlSecuredBy.class)).map(v -> newHashSet(v.value())).orElse(newHashSet()),
                            Optional.ofNullable(method.getAnnotation(RamlTraits.class)).map(v -> newHashSet(v.value())).orElse(newHashSet()),
                            queryParameters,
                            headers,
                            responses
                    )
            );
    }

    @VisibleForTesting
    protected void setParametersFactory(ParametersFactory parametersFactory) {
        this.parametersFactory = parametersFactory;
    }

    @VisibleForTesting
    protected void setResponseFactory(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }
}
