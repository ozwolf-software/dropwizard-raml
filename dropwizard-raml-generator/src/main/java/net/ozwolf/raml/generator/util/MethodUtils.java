package net.ozwolf.raml.generator.util;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodUtils {
    public static String getAction(Method method) {
        if (method.isAnnotationPresent(GET.class))
            return "GET";

        if (method.isAnnotationPresent(POST.class))
            return "POST";

        if (method.isAnnotationPresent(PUT.class))
            return "PUT";

        if (method.isAnnotationPresent(DELETE.class))
            return "DELETE";

        if (Arrays.stream(method.getAnnotations()).anyMatch(a -> a.getClass().getName().equalsIgnoreCase("io.dropwizard.jersey.PATCH")))
            return "PATCH";

        return null;
    }

    public static boolean hasBody(Method method){
        String action = getAction(method);
        if (action == null)
            return false;

        return action.equalsIgnoreCase("POST") || action.equalsIgnoreCase("PUT") || action.equalsIgnoreCase("PATCH");
    }
}
