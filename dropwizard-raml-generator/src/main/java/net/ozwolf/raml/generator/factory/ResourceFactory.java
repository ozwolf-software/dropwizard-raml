package net.ozwolf.raml.generator.factory;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.annotations.RamlIgnore;
import net.ozwolf.raml.annotations.RamlResource;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlAction;
import net.ozwolf.raml.generator.model.RamlMethodModel;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import net.ozwolf.raml.generator.model.RamlResourceModel;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.collect.Maps.newHashMap;

public class ResourceFactory {
    private ParametersFactory parametersFactory;
    private MethodFactory methodFactory;

    public ResourceFactory() {
        this.parametersFactory = new ParametersFactory();
        this.methodFactory = new MethodFactory();
    }

    public void apply(Class<?> resource, Consumer<RamlResourceModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Path path = resource.getAnnotation(Path.class);
        RamlResource annotation = resource.getAnnotation(RamlResource.class);

        if (path == null || annotation == null) {
            if (path == null)
                onError.accept(new RamlGenerationError(resource, "missing [ @" + Path.class.getSimpleName() + " ] annotation"));
            if (annotation == null)
                onError.accept(new RamlGenerationError(resource, "missing [ @" + RamlResource.class.getSimpleName() + " ] annotation"));
            return;
        }

        CheckedOnError e = new CheckedOnError(onError);

        Map<String, RamlParameterModel> uriParameters = newHashMap();
        parametersFactory.getUriParameters(resource, p -> uriParameters.put(p.getName(), p), e);

        RamlResourceModel model = new RamlResourceModel(path.value(), annotation.displayName(), annotation.description(), uriParameters);
        applyMethods(resource, null, model::addMethod, e);
        applySubResources(resource, model::addSubResource, e);

        if (!e.isInError())
            onSuccess.accept(model);
    }

    @VisibleForTesting
    protected void setParametersFactory(ParametersFactory parametersFactory) {
        this.parametersFactory = parametersFactory;
    }

    @VisibleForTesting
    protected void setMethodFactory(MethodFactory methodFactory) {
        this.methodFactory = methodFactory;
    }

    private void apply(Method method, Consumer<RamlResourceModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Path path = method.getAnnotation(Path.class);

        if (path == null) {
            onError.accept(new RamlGenerationError(method.getDeclaringClass(), method, "missing [ @" + Path.class.getSimpleName() + " ] on expected sub-resource."));
            return;
        }

        CheckedOnError e = new CheckedOnError(onError);

        Map<String, RamlParameterModel> uriParameters = newHashMap();
        parametersFactory.getUriParameters(method, p -> uriParameters.put(p.getName(), p), e);

        RamlResourceModel model = new RamlResourceModel(
                path.value(),
                null,
                null,
                uriParameters);

        applyMethods(method.getDeclaringClass(), path, model::addMethod, e);

        if (!e.isInError())
            onSuccess.accept(model);
    }

    private void applySubResources(Class<?> resource, Consumer<RamlResourceModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Arrays.stream(resource.getDeclaredMethods())
                .filter(m -> !m.isAnnotationPresent(RamlIgnore.class))
                .filter(m -> m.isAnnotationPresent(Path.class))
                .forEach(m -> apply(m, onSuccess, onError));
    }

    private void applyMethods(Class<?> resource, Path path, Consumer<RamlMethodModel> onSuccess, Consumer<RamlGenerationError> onError) {
        Arrays.stream(resource.getDeclaredMethods())
                .filter(m -> !m.isAnnotationPresent(RamlIgnore.class))
                .filter(m -> {
                    String resourcePath = Optional.ofNullable(path).map(Path::value).orElse(null);
                    String methodPath = Optional.ofNullable(m.getAnnotation(Path.class)).map(Path::value).orElse(null);
                    return Objects.equals(resourcePath, methodPath);
                })
                .filter(m -> RamlAction.getActionFor(m).isPresent())
                .forEach(m -> methodFactory.getMethod(m, onSuccess, onError));
    }
}
