package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.jersey.PATCH;
import net.ozwolf.raml.annotations.RamlParameter;
import net.ozwolf.raml.annotations.RamlResource;
import net.ozwolf.raml.annotations.RamlUriParameters;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.uri.UriTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;

@JsonSerialize
@JsonPropertyOrder({"displayName", "description", "uriParameters"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlResourceModel {
    private final String path;
    private final String displayName;
    private final String description;
    private final Map<String, RamlParameterModel> uriParameters;
    private final Map<String, RamlMethodModel> methods;
    private final Class<?> resource;

    public RamlResourceModel(Class<?> resource) {
        Path path = resource.getAnnotation(Path.class);
        RamlResource annotation = resource.getAnnotation(RamlResource.class);

        if (path == null || annotation == null)
            throw new IllegalStateException("Resource [ " + resource.getSimpleName() + " ] is missing [ @" + Path.class.getSimpleName() + " ] or [ @" + RamlResource.class.getSimpleName() + " ] annotations.");

        UriTemplate template = new UriTemplate(path.value());
        if (template.getNumberOfTemplateVariables() > 0){
            RamlUriParameters uriParameters = resource.getAnnotation(RamlUriParameters.class);
            if (uriParameters == null)
                throw new IllegalStateException("Resource [ " + resource.getSimpleName() + " ] has URI parameters but is missing the [ @" + RamlUriParameters.class.getSimpleName() + " ] annotation to describe them.");

            this.uriParameters = Arrays.stream(uriParameters.value()).collect(toMap(RamlParameter::name, RamlParameterModel::new));
            List<String> missed = template.getTemplateVariables().stream().filter(v -> !this.uriParameters.containsKey(v)).collect(toList());
            if (!missed.isEmpty())
                throw new IllegalStateException("URI parameters [ " + StringUtils.join(missed, ", ") + " ] on resource [ " + resource.getSimpleName() + " ] have not been described.");

            List<String> extra = this.uriParameters.keySet().stream().filter(v -> !template.getTemplateVariables().contains(v)).collect(toList());
            if (!extra.isEmpty())
                throw new IllegalStateException("URI parameters [ " + StringUtils.join(extra, ", ") + " ] on resource [ " + resource.getSimpleName() + " ] have been described but are not part of the path [ " + path.value() + " ]");
        } else {
            this.uriParameters = null;
        }

        this.path = path.value();
        this.displayName = annotation.displayName();
        this.description = annotation.description();
        this.methods = getMethods(null, resource).entrySet().stream().collect(toMap(Map.Entry::getKey, e -> new RamlMethodModel(e.getKey(), e.getValue())));

        this.resource = resource;
    }

    @JsonIgnore
    public String getPath() {
        return path;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("uriParameters")
    public Map<String, RamlParameterModel> getUriParameters(){
        return nullIfEmpty(uriParameters);
    }

    @JsonAnyGetter
    public Map<String, RamlMethodModel> getMethods(){
        return methods;
    }

    private static Map<String, Method> getMethods(Path path, Class<?> resource) {
        if (path == null) {
            return Arrays.stream(resource.getMethods())
                    .filter(m -> !m.isAnnotationPresent(Path.class) && hasSupportedAction(m))
                    .collect(toMap(RamlResourceModel::getActionName, m -> m));
        } else {
            return Arrays.stream(resource.getMethods())
                    .filter(m -> m.isAnnotationPresent(Path.class) && m.getAnnotation(Path.class).value().equals(path.value()))
                    .filter(RamlResourceModel::hasSupportedAction)
                    .collect(toMap(RamlResourceModel::getActionName, m -> m));
        }
    }

    private static String getActionName(Method method) {
        if (method.isAnnotationPresent(GET.class)) return "get";
        if (method.isAnnotationPresent(DELETE.class)) return "delete";
        if (method.isAnnotationPresent(PUT.class)) return "put";
        if (method.isAnnotationPresent(POST.class)) return "post";
        if (method.isAnnotationPresent(PATCH.class)) return "patch";

        throw new IllegalStateException("Unsupported action for method.");
    }

    private static boolean hasSupportedAction(Method method) {
        return method.isAnnotationPresent(GET.class) ||
                method.isAnnotationPresent(DELETE.class) ||
                method.isAnnotationPresent(PUT.class) ||
                method.isAnnotationPresent(POST.class) ||
                method.isAnnotationPresent(PATCH.class);
    }
}
