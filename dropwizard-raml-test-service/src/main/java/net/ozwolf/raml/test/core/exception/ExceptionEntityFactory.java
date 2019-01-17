package net.ozwolf.raml.test.core.exception;

import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Maps.newHashMap;

public class ExceptionEntityFactory {
    public static Map<String, Object> createFrom(String message) {
        return createFrom(message, null, null);
    }

    public static Map<String, Object> createFrom(String message, String otherProperty, Object otherValue) {
        Map<String, Object> entity = newHashMap();
        entity.put("reference", UUID.randomUUID().toString());
        entity.put("serviceName", "dropwizard-raml-test-service");
        entity.put("message", message);
        if (otherProperty != null)
            entity.put(otherProperty, otherValue);
        return entity;
    }
}
