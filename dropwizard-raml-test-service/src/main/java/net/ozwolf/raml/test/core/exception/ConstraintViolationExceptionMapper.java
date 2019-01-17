package net.ozwolf.raml.test.core.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.joining;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = newHashMap();
        exception.getConstraintViolations().forEach(v -> errors.put(getPathName(v.getPropertyPath()), v.getMessage()));

        LOGGER.warn("Request failed validation");

        return Response.status(422)
                .entity(ExceptionEntityFactory.createFrom("Request failed validation", "errors", errors))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    private static String getPathName(Path p) {
        return StreamSupport.stream(p.spliterator(), false)
                .filter(n -> !StringUtils.isBlank(n.getName()) && n.getKind() != ElementKind.METHOD)
                .map(Object::toString)
                .collect(joining("."));
    }
}
