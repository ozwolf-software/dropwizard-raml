package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.RamlRequests;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlAction;
import net.ozwolf.raml.generator.model.RamlBodyModel;

import javax.validation.Valid;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

public class RequestFactory {
    public void getRequests(Method method, Consumer<RamlBodyModel> onSuccess, Consumer<RamlGenerationError> onError) {
        RamlRequests annotation = method.getAnnotation(RamlRequests.class);

        if (!RamlAction.find(method).map(RamlAction::hasBody).orElse(false))
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
