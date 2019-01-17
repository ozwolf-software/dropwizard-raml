package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.*;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import org.glassfish.jersey.uri.UriTemplate;

import javax.ws.rs.FormParam;
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

class ParametersFactory {
    void getUriParameters(Class<?> resource, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Path path = resource.getAnnotation(Path.class);
        RamlUriParameters annotation = resource.getAnnotation(RamlUriParameters.class);

        if (path == null)
            return;

        UriTemplate template = new UriTemplate(path.value());
        if (template.getNumberOfTemplateVariables() == 0)
            return;

        if (annotation == null) {
            onError.accept(new RamlGenerationError(resource, template.getTemplate() + " : has URI parameters but resource is missing [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation"));
            return;
        }

        getUriParameters(
                template,
                template,
                annotation.value(),
                onSuccess,
                m -> onError.accept(new RamlGenerationError(resource, m))
        );
    }

    void getUriParameters(Class<?> resource, Path path, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
        RamlUriParameters annotation = resource.getAnnotation(RamlUriParameters.class);

        UriTemplate resourceTemplate = new UriTemplate(resource.getAnnotation(Path.class).value() + path.value());

        UriTemplate template = new UriTemplate(path.value());
        if (template.getNumberOfTemplateVariables() == 0)
            return;

        if (annotation == null) {
            onError.accept(new RamlGenerationError(resource, resourceTemplate.getTemplate() + " : has URI parameters but resource is missing [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation"));
            return;
        }

        getUriParameters(
                resourceTemplate,
                template,
                annotation.value(),
                onSuccess,
                m -> onError.accept(new RamlGenerationError(resource, m))
        );
    }

    void getQueryParameters(Method method, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
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

    void getHeaders(Method method, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
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

    void getFormParameters(Method method, Consumer<RamlParameterModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Map<String, RamlParameter> descriptions = Optional.ofNullable(method.getAnnotation(RamlFormParameters.class))
                .map(a -> Arrays.stream(a.value()).collect(toMap(RamlParameter::name, v -> v)))
                .orElse(newHashMap());

        getMethodParameters(
                p -> p.isAnnotationPresent(FormParam.class),
                p -> p.getAnnotation(FormParam.class).value(),
                descriptions,
                method,
                onSuccess,
                onError
        );
    }

    private static void getUriParameters(UriTemplate resourceTemplate,
                                         UriTemplate template,
                                         RamlParameter[] descriptions,
                                         Consumer<RamlParameterModel> onSuccess,
                                         Consumer<String> onError) {


        Map<String, RamlParameterModel> parameters = Arrays.stream(descriptions)
                .filter(d -> template.getTemplateVariables().stream().anyMatch(t -> t.equals(d.name())))
                .collect(toMap(RamlParameter::name, RamlParameterModel::new));

        List<String> errors = newArrayList();

        List<String> templateVariables = template.getTemplateVariables();
        templateVariables
                .stream()
                .filter(v -> !parameters.containsKey(v))
                .forEach(v -> errors.add(resourceTemplate.getTemplate() + " : parameter [ " + v + " ] has no [ @" + RamlParameter.class.getSimpleName() + " ] definition"));

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
