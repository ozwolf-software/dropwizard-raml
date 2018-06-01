package net.ozwolf.raml.generator.model;

import io.dropwizard.jersey.PATCH;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public enum RamlAction {
    get(GET.class),
    post(POST.class),
    put(PUT.class),
    delete(DELETE.class),
    patch(PATCH.class);

    private final Class<? extends Annotation> method;

    RamlAction(Class<? extends Annotation> method) {
        this.method = method;
    }

    public boolean hasBody() {
        return this == post || this == patch || this == put;
    }

    public static Optional<RamlAction> find(Method method) {
        return Arrays.stream(values())
                .filter(v -> method.isAnnotationPresent(v.method))
                .findFirst();
    }
}
