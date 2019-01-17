package net.ozwolf.raml.monitor.filter;

import net.ozwolf.raml.annotations.RamlIgnore;
import net.ozwolf.raml.common.model.RamlApplication;
import net.ozwolf.raml.common.model.RamlMethod;
import net.ozwolf.raml.common.model.RamlResource;
import net.ozwolf.raml.common.model.v08.V08_RamlApplication;
import net.ozwolf.raml.common.model.v10.V10_RamlApplication;
import net.ozwolf.raml.generator.RamlSpecification;
import net.ozwolf.raml.monitor.ApiMonitorBundle;
import net.ozwolf.raml.monitor.annotation.DoNotMonitor;

import javax.ws.rs.*;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

public class RamlMonitorFeature implements DynamicFeature {
    private final RamlSpecification specification;
    private final ExecutorService executor;

    public RamlMonitorFeature(RamlSpecification specification, ExecutorService executor) {
        this.specification = specification;
        this.executor = executor;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        Class<?> resource = resourceInfo.getResourceClass();
        Method method = resourceInfo.getResourceMethod();

        boolean ignored = resource.isAnnotationPresent(RamlIgnore.class) || method.isAnnotationPresent(RamlIgnore.class);
        boolean notMonitored = resource.isAnnotationPresent(DoNotMonitor.class) || method.isAnnotationPresent(DoNotMonitor.class);

        if (ignored || notMonitored) return;

        Path resourcePath = resource.getAnnotation(Path.class);
        if (resourcePath == null)
            return;

        Path methodPath = method.getAnnotation(Path.class);

        RamlApplication application = getApplication();
        if (application == null)
            return;

        RamlResource resourceSpec = application.find(resourcePath.value(), methodPath == null ? null : methodPath.value()).orElse(null);
        if (resourceSpec == null) {
            ApiMonitorBundle.LOGGER.warn("Method [ " + resource.getSimpleName() + "." + method.getName() + " ] has no matching resource specification and will not be monitored.");
            return;
        }

        String methodName = MethodType.findFor(method);
        if (methodName == null)
            return;

        RamlMethod methodSpec = resourceSpec.find(methodName).orElse(null);
        if (methodSpec == null) {
            ApiMonitorBundle.LOGGER.warn("Method [ " + resource.getSimpleName() + "." + method.getName() + " ] has no matching method specification and will not be monitored.");
            return;
        }

        RamlMonitor monitor = new RamlMonitor(executor);

        context.register(new RamlMonitorRequestFilter(resourceSpec, methodSpec, method));
        context.register(new RamlMonitorResponseFilter(methodSpec, monitor, method));
        context.register(new RamlMonitorResponseWriterInterceptor(monitor));
    }

    private RamlApplication getApplication() {
        return specification.getModel()
                .map(m -> {
                    if (m.isVersion10()) return new V10_RamlApplication(m.getApiV10());
                    if (m.isVersion08()) return new V08_RamlApplication(m.getApiV08());
                    return null;
                }).orElse(null);
    }

    private enum MethodType {
        METHOD_GET(GET.class, "get"),
        METHOD_DELETE(DELETE.class, "delete"),
        METHOD_PUT(PUT.class, "put"),
        METHOD_POST(POST.class, "post"),
        METHOD_HEAD(HEAD.class, "head"),
        METHOD_OPTIONS(OPTIONS.class, "options");

        private final Class<? extends Annotation> annotation;
        private final String type;

        MethodType(Class<? extends Annotation> annotation, String type) {
            this.annotation = annotation;
            this.type = type;
        }

        public static String findFor(Method method) {
            return Arrays.stream(values())
                    .filter(t -> method.isAnnotationPresent(t.annotation))
                    .findFirst()
                    .map(t -> t.type)
                    .orElse(null);
        }
    }
}
