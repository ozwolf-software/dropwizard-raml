package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.RamlHeaders;
import net.ozwolf.raml.annotations.RamlParameter;
import net.ozwolf.raml.annotations.RamlQueryParameters;
import net.ozwolf.raml.annotations.RamlUriParameters;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import org.glassfish.jersey.uri.UriTemplate;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ParametersFactory {
    public static void getUriParameters(Class<?> resource, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
        getUriParameters(
                resource.getAnnotation(Path.class),
                resource.getAnnotation(RamlUriParameters.class),
                onSuccess,
                m -> onError.accept(new RamlGenerationError(resource, m))
        );
    }

    public static void getUriParameters(Method method, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
        getUriParameters(
                method.getAnnotation(Path.class),
                method.getAnnotation(RamlUriParameters.class),
                onSuccess,
                m -> onError.accept(new RamlGenerationError(method.getDeclaringClass(), method, m))
        );
    }

    public static void getQueryParameters(Method method, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Map<String, RamlParameter> descriptions = Optional.ofNullable(method.getAnnotation(RamlQueryParameters.class))
                .map(a -> Arrays.stream(a.value()).collect(toMap(RamlParameter::name, v -> v)))
                .orElse(newHashMap());

        getMethodParameters(
                p -> p.isAnnotationPresent(QueryParam.class),
                p -> p.getAnnotation(QueryParam.class).value(),
                descriptions,
                method,
                onSuccess,
                onError
        );
    }

    public static void getHeaders(Method method, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Map<String, RamlParameter> descriptions = Optional.ofNullable(method.getAnnotation(RamlHeaders.class))
                .map(a -> Arrays.stream(a.value()).collect(toMap(RamlParameter::name, v -> v)))
                .orElse(newHashMap());

        getMethodParameters(
                p -> p.isAnnotationPresent(HeaderParam.class),
                p -> p.getAnnotation(HeaderParam.class).value(),
                descriptions,
                method,
                onSuccess,
                onError
        );
    }

    private static void getUriParameters(Path path,
                                         RamlUriParameters annotation,
                                         Consumer<RamlParameterModel> onSuccess,
                                         Consumer<String> onError) {
        if (path == null)
            return;

        UriTemplate template = new UriTemplate(path.value());
        if (template.getNumberOfTemplateVariables() == 0)
            return;

        if (annotation == null) {
            onError.accept("has URI parameters but is missing [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation");
            return;
        }

        Map<String, RamlParameterModel> parameters = Arrays.stream(annotation.value()).collect(toMap(RamlParameter::name, RamlParameterModel::new));

        List<String> errors = newArrayList();

        List<String> templateVariables = template.getTemplateVariables();
        templateVariables
                .stream()
                .filter(v -> !parameters.containsKey(v))
                .forEach(v -> errors.add("parameter [ " + v + " ] has no [ @" + RamlParameter.class.getSimpleName() + " ] definition"));

        parameters.keySet()
                .stream()
                .filter(k -> !templateVariables.contains(k))
                .forEach(k -> errors.add("parameter [ " + k + " ] described but is not in path"));

        if (errors.isEmpty()) {
            parameters.values().forEach(onSuccess);
        } else {
            errors.forEach(onError);
        }
    }

    private static void getMethodParameters(Function<Parameter, Boolean> filter,
                                            Function<Parameter, String> name,
                                            Map<String, RamlParameter> descriptions,
                                            Method method,
                                            Consumer<RamlParameterModel> onSuccess,
                                            Consumer<RamlGenerationError> onError) {
        List<Parameter> parameters = Arrays.stream(method.getParameters())
                .filter(filter::apply)
                .collect(toList());

        if (parameters.isEmpty()) return;

        parameters.stream()
                .map(p -> {
                    String parameterName = name.apply(p);
                    return descriptions.containsKey(parameterName) ? new RamlParameterModel(descriptions.get(parameterName)) : new RamlParameterModel(parameterName, p);
                })
                .forEach(onSuccess);
    }
}
