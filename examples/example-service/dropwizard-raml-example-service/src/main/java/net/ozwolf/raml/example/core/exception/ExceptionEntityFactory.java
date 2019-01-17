package net.ozwolf.raml.example.core.exception;

import javax.ws.rs.WebApplicationException;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Maps.newHashMap;

public class ExceptionEntityFactory {
    public static Map<String, Object> createFrom(String message){
        Map<String, Object> entity = newHashMap();
        entity.put("reference", UUID.randomUUID().toString());
        entity.put("serviceName", "dropwizard-raml-test-service");
        entity.put("message", message);
        return entity;
    }
}
