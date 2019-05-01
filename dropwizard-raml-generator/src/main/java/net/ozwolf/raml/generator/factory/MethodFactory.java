package net.ozwolf.raml.generator.factory;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.annotations.RamlDeprecated;
import net.ozwolf.raml.annotations.RamlDescription;
import net.ozwolf.raml.annotations.RamlIs;
import net.ozwolf.raml.annotations.RamlSecuredBy;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlBodyModel;
import net.ozwolf.raml.generator.model.RamlMethodModel;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import net.ozwolf.raml.generator.model.RamlResponseModel;
import net.ozwolf.raml.generator.util.MethodUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

/**
 * <h1>Method Factory</h1>
 *
 * A factory used to derive resource methods into RAML methods.
 */
class MethodFactory {
    private ParametersFactory parametersFactory;
    private ResponseFactory responseFactory;
    private RequestFactory requestFactory;

    MethodFactory() {
        this.parametersFactory = new ParametersFactory();
        this.responseFactory = new ResponseFactory();
        this.requestFactory = new RequestFactory();
    }

    /**
     * Retrieve the RAML method model from the provided resource method.
     *
     * @param method    the resource method
     * @param onSuccess the on success handler
     * @param onError   the on error handler
     */
    void getMethod(Method method, Map<Integer, RamlResponseModel> globalResponses, Consumer<RamlMethodModel> onSuccess, Consumer<RamlGenerationError> onError) {
        String action = MethodUtils.getAction(method);
        if (action == null) {
            onError.accept(new RamlGenerationError(method.getDeclaringClass(), method, "unsupported action"));
            return;
        }

        CheckedOnError e = new CheckedOnError(onError);

        Map<String, RamlParameterModel> queryParameters = newHashMap();
        parametersFactory.getQueryParameters(method, p -> queryParameters.put(p.getName(), p), e);

        Map<String, RamlParameterModel> headers = newHashMap();
        parametersFactory.getHeaders(method, p -> headers.put(p.getName(), p), e);

        Map<String, RamlParameterModel> formParameters = newHashMap();
        parametersFactory.getFormParameters(method, p -> formParameters.put(p.getName(), p), e);

        Map<Integer, RamlResponseModel> responses = newHashMap();
        responseFactory.getResponses(method, m -> responses.put(m.getStatus(), m), e);

        Map<String, RamlBodyModel> requests = newHashMap();
        if (MethodUtils.hasBody(method)) {
            requestFactory.getRequests(method, b -> requests.put(b.getContentType(), b), e);
        }

        if (!e.isInError())
            onSuccess.accept(
                    new RamlMethodModel(
                            action,
                            Optional.ofNullable(method.getAnnotation(RamlDescription.class)).map(RamlDescription::value).orElse(null),
                            Optional.ofNullable(method.getAnnotation(RamlSecuredBy.class)).map(v -> newHashSet(v.value())).orElse(newHashSet()),
                            getTraits(method),
                            queryParameters,
                            headers,
                            formParameters,
                            requests,
                            responses
                    ).apply(globalResponses)
            );
    }

    private Set<String> getTraits(Method method) {
        Set<String> traits = newHashSet();
        if (method.isAnnotationPresent(RamlDeprecated.class))
            traits.add(RamlDeprecated.FLAG);

        if (method.isAnnotationPresent(RamlIs.class))
            traits.addAll(newHashSet(method.getAnnotation(RamlIs.class).value()));
        return traits;
    }

    @VisibleForTesting
    void setParametersFactory(ParametersFactory parametersFactory) {
        this.parametersFactory = parametersFactory;
    }

    @VisibleForTesting
    void setResponseFactory(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @VisibleForTesting
    void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }
}
