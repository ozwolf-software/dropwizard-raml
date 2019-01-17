package net.ozwolf.raml.monitor.filter;

import net.ozwolf.raml.monitor.ApiMonitorBundle;
import net.ozwolf.raml.monitor.managed.RamlMonitorScope;
import net.ozwolf.raml.monitor.managed.RamlMonitorScopeManager;
import net.ozwolf.raml.validator.ApiRequest;
import net.ozwolf.raml.validator.ApiResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RamlMonitorResponseWriterInterceptor implements WriterInterceptor {
    private final RamlMonitor monitor;

    public RamlMonitorResponseWriterInterceptor(RamlMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        String scopeId = (String) context.getHeaders().getFirst(ApiMonitorBundle.SCOPE_HEADER);
        RamlMonitorScope scope = scopeId == null ? null : RamlMonitorScopeManager.instant().take(scopeId);
        if (scope == null) {
            context.proceed();
        } else {
            ApiRequest request = scope.getRequest();
            ApiResponse response = scope.getResponse().withContent(getContent(context)).build();

            monitor.validate(request, response);
        }
    }

    private byte[] getContent(WriterInterceptorContext context) throws IOException {
        OutputStream out = context.getOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            context.setOutputStream(baos);
            context.proceed();
        } finally {
            baos.writeTo(out);
            baos.close();
            context.setOutputStream(out);
        }
        return baos.toByteArray();
    }
}
