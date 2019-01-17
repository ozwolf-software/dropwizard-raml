package net.ozwolf.raml.monitor.filter;

import net.ozwolf.raml.common.model.RamlMethod;
import net.ozwolf.raml.common.model.RamlResource;
import net.ozwolf.raml.monitor.ApiMonitorBundle;
import net.ozwolf.raml.monitor.managed.RamlMonitorScopeManager;
import net.ozwolf.raml.validator.ApiRequest;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

public class RamlMonitorRequestFilter implements ContainerRequestFilter {
    private final RamlResource resourceDefinition;
    private final RamlMethod methodDefintion;
    private final Method method;

    public RamlMonitorRequestFilter(RamlResource resourceDefinition,
                                    RamlMethod methodDefintion,
                                    Method method) {
        this.resourceDefinition = resourceDefinition;
        this.methodDefintion = methodDefintion;
        this.method = method;
    }

    @Override
    public void filter(ContainerRequestContext request) {
        try {
            byte[] content = null;
            if (request.hasEntity()) {
                content = IOUtils.toByteArray(request.getEntityStream());
                request.setEntityStream(new ByteArrayInputStream(content));
            }

            ApiRequest descriptor = new ApiRequest.Builder(resourceDefinition, methodDefintion, "/" + request.getUriInfo().getPath(), getMediaType(request))
                    .withHeaders(request.getHeaders())
                    .withPathParameters(request.getUriInfo().getPathParameters())
                    .withQueryParameters(request.getUriInfo().getQueryParameters())
                    .withContent(content)
                    .build();

            String scopeId = RamlMonitorScopeManager.instant().addRequest(descriptor);
            request.getHeaders().putSingle(ApiMonitorBundle.SCOPE_HEADER, scopeId);
        } catch (Throwable t) {
            ApiMonitorBundle.LOGGER.error("Error retrieving request content for monitoring.  Monitoring aborted.", t);
        }
    }

    private MediaType getMediaType(ContainerRequestContext request) {
        if (!request.getMethod().equalsIgnoreCase("PUT") && !request.getMethod().equalsIgnoreCase("POST"))
            return null;

        MediaType mediaType = request.getMediaType();
        if (mediaType != null)
            return mediaType;

        Consumes annotation = method.getAnnotation(Consumes.class);
        if (annotation != null)
            return MediaType.valueOf(annotation.value()[0]);

        return MediaType.valueOf(request.getHeaderString(HttpHeaders.CONTENT_TYPE));
    }
}
