package net.ozwolf.raml.monitor.filter;

import net.ozwolf.raml.common.model.RamlMethod;
import net.ozwolf.raml.monitor.ApiMonitorBundle;
import net.ozwolf.raml.monitor.managed.RamlMonitorScope;
import net.ozwolf.raml.monitor.managed.RamlMonitorScopeManager;
import net.ozwolf.raml.validator.ApiRequest;
import net.ozwolf.raml.validator.ApiResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

public class RamlMonitorResponseFilter implements ContainerResponseFilter {
    private final RamlMethod methodDescriptor;
    private final RamlMonitor monitor;
    private final Method method;

    public RamlMonitorResponseFilter(RamlMethod methodDescriptor,
                                     RamlMonitor monitor,
                                     Method method) {
        this.methodDescriptor = methodDescriptor;
        this.monitor = monitor;
        this.method = method;
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        String scopeId = request.getHeaderString(ApiMonitorBundle.SCOPE_HEADER);
        RamlMonitorScope scope = scopeId == null ? null : RamlMonitorScopeManager.instant().borrow(scopeId);
        if (scope == null)
            return;

        ApiResponse.Builder builder = new ApiResponse.Builder(methodDescriptor, response.getStatus(), getMediaType(request, response))
                .withHeaders(response.getStringHeaders());

        if (response.getEntity() == null) { // A null response doesn't need parsing at the response writer level
            scope = RamlMonitorScopeManager.instant().take(scopeId);
            ApiRequest apiRequest = scope.getRequest();
            ApiResponse apiResponse = builder.build();
            monitor.validate(apiRequest, apiResponse);
        } else if (response.getEntity() instanceof String) { // A string response doesn't need parsing at the response writer level
            scope = RamlMonitorScopeManager.instant().take(scopeId);
            ApiRequest apiRequest = scope.getRequest();
            ApiResponse apiResponse = builder.withContent(((String) response.getEntity()).getBytes()).build();
            monitor.validate(apiRequest, apiResponse);
        } else { // Other response entities need a writer, thus the bytes must be handled in a writer interceptor
            response.getHeaders().putSingle(ApiMonitorBundle.SCOPE_HEADER, scopeId);
            scope.withResponse(builder);
        }
    }

    private MediaType getMediaType(ContainerRequestContext request, ContainerResponseContext response) {
        MediaType mediaType = response.getMediaType();
        if (mediaType != null)
            return mediaType;

        Produces annotation = method.getAnnotation(Produces.class);
        if (annotation != null)
            return MediaType.valueOf(annotation.value()[0]);

        return Optional.ofNullable(request.getHeaderString(HttpHeaders.ACCEPT)).map(MediaType::valueOf).orElse(null);
    }
}
