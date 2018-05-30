package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.RamlResponses;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlResponseModel;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.common.collect.Sets.newHashSet;

public class ResponseFactory {
    public static void create(Method method, Consumer<RamlResponseModel> onSuccess, Consumer<RamlGenerationError> onError) {
        RamlResponses annotation = method.getAnnotation(RamlResponses.class);

        if (annotation != null) {
            Arrays.stream(annotation.value()).forEach(a -> onSuccess.accept(new RamlResponseModel(a)));
            return;
        }

        Set<String> contentTypes = Optional.ofNullable(method.getAnnotation(Produces.class)).map(v -> newHashSet(v.value())).orElse(newHashSet("application/json"));

        Class<?> returnType = method.getReturnType();
        if (returnType == Response.class) {
            onSuccess.accept(new RamlResponseModel(200, contentTypes));
        } else {
            onSuccess.accept(new RamlResponseModel(200, contentTypes, returnType));
        }
    }
}
