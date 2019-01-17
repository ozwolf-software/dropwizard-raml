package net.ozwolf.raml.example.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        String reason = exception.getResponse().getStatusInfo().getReasonPhrase();

        if (exception.getResponse().getStatus() < 500) {
            LOGGER.warn("Client error: " + exception.getResponse().getStatus() + " - " + reason);
        } else {
            LOGGER.error("Server error: " + exception.getResponse().getStatus() + " - " + reason, exception);
        }

        return Response.status(exception.getResponse().getStatus())
                .entity(ExceptionEntityFactory.createFrom(reason))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
