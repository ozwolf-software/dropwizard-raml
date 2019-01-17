package net.ozwolf.raml.example.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UnhandledExceptionMapper implements ExceptionMapper<Exception> {
    private final static Logger LOGGER = LoggerFactory.getLogger(UnhandledExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error("Error handling request.", exception);

        return Response.status(500)
                .entity(ExceptionEntityFactory.createFrom("Internal Server Error"))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
