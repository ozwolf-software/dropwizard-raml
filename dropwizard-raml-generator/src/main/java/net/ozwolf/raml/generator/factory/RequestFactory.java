package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.RamlRequests;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlBodyModel;
import net.ozwolf.raml.generator.util.MethodUtils;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * <h1>Request Factory</h1>
 *
 * A factory for creating request body specifications.
 */
class RequestFactory {
    /**
     * Retrieve the request body specification for the provided resource method
     *
     * @param method    the resource method
     * @param onSuccess the on success handler
     * @param onError   the on error handler
     */
    void getRequests(Method method, Consumer<RamlBodyModel> onSuccess, Consumer<RamlGenerationError> onError) {
        RamlRequests annotation = method.getAnnotation(RamlRequests.class);

        if (!MethodUtils.hasBody(method))
            return;

        if (annotation != null) {
            Arrays.stream(annotation.value()).forEach(b -> onSuccess.accept(new RamlBodyModel(b)));
        } else {
            boolean hasBody = Arrays.stream(method.getParameters())
                    .anyMatch(p ->
                            p.getAnnotations().length == 0 || (p.getAnnotations().length == 1 && p.isAnnotationPresent(Valid.class)));

            if (hasBody)
                onError.accept(new RamlGenerationError(method.getDeclaringClass(), method, "request body entity found but missing [ @" + RamlRequests.class.getSimpleName() + " ] annotation"));
        }
    }
}
