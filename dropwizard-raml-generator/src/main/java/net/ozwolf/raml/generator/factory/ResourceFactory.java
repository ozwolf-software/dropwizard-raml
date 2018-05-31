package net.ozwolf.raml.generator.factory;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.annotations.RamlIgnore;
import net.ozwolf.raml.annotations.RamlSubResource;
import net.ozwolf.raml.annotations.RamlSubResources;
import net.ozwolf.raml.annotations.RamlResource;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlAction;
import net.ozwolf.raml.generator.model.RamlParameterModel;
import net.ozwolf.raml.generator.model.RamlResourceModel;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class ResourceFactory {
    private ParametersFactory parametersFactory;
    private MethodFactory methodFactory;

    public ResourceFactory() {
        this.parametersFactory = new ParametersFactory();
        this.methodFactory = new MethodFactory();
    }

    public void getResource(Class<?> resource, Consumer<RamlResourceModel> onSuccess, Consumer<RamlGenerationError> onError) {
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

        Map<Path, List<Method>> methods = newHashMap();
        Arrays.stream(resource.getMethods())
                .filter(m -> !m.isAnnotationPresent(RamlIgnore.class))
                .filter(m -> !m.isAnnotationPresent(Path.class))
                .filter(m -> RamlAction.getActionFor(m).isPresent())
                .forEach(m -> methods.computeIfAbsent(path, k -> newArrayList()).add(m));
        Arrays.stream(resource.getMethods())
                .filter(m -> !m.isAnnotationPresent(RamlIgnore.class))
                .filter(m -> m.isAnnotationPresent(Path.class))
                .filter(m -> RamlAction.getActionFor(m).isPresent())
                .forEach(m -> methods.computeIfAbsent(m.getAnnotation(Path.class), k -> newArrayList()).add(m));

        methods.get(path).forEach(m -> methodFactory.getMethod(m, model::addMethod, e));
        methods.entrySet()
                .stream()
                .filter(r -> !r.getKey().equals(path))
                .forEach(r -> getSubResource(resource, r.getKey(), r.getValue(), model::addSubResource, e));

        if (!e.isInError())
            onSuccess.accept(model);
    }

    private void getSubResource(Class<?> resource, Path path, List<Method> methods, Consumer<RamlResourceModel> onSuccess, Consumer<RamlGenerationError> onError) {
        RamlSubResource information = Optional.ofNullable(resource.getAnnotation(RamlSubResources.class))
                .map(a -> Arrays.stream(a.value()).filter(p -> p.path().equals(path)).findFirst().orElse(null))
                .orElse(null);

        CheckedOnError e = new CheckedOnError(onError);

        String description = information == null ? null : information.description();
        Map<String, RamlParameterModel> uriParameters = newHashMap();
        parametersFactory.getUriParameters(resource, path, p -> uriParameters.put(p.getName(), p), e);

        RamlResourceModel model = new RamlResourceModel(path.value(), null, description, uriParameters);

        methods.forEach(m -> methodFactory.getMethod(m, model::addMethod, e));

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
}
