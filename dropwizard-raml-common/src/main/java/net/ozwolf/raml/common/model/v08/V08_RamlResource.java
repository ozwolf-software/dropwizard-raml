package net.ozwolf.raml.common.model.v08;

import net.ozwolf.raml.common.model.RamlMethod;
import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.common.model.RamlResource;
import org.raml.v2.api.model.v08.resources.Resource;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 0.8 Resource</h1>
 *
 * A 0.8 definition of an API resource
 */
public class V08_RamlResource implements RamlResource {
    private final Resource resource;

    /**
     * Create a new 0.8 API resource item
     *
     * @param resource the resource
     */
    public V08_RamlResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDisplayName() {
        return resource.displayName();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(resource.description()).map(StringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPath() {
        return resource.resourcePath();
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlParameter> getUriParameters() {
        Map<String, RamlParameter> parameters = newHashMap();
        addParameters(resource, parameters);
        return newArrayList(parameters.values());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlMethod> getMethods() {
        return resource.methods() == null ? newArrayList() : resource.methods().stream().map(V08_RamlMethod::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlResource> getResources() {
        return resource.resources() == null ? newArrayList() : resource.resources().stream().map(V08_RamlResource::new).collect(toList());
    }

    private void addParameters(Resource resource, Map<String, RamlParameter> parameters) {
        if (resource.uriParameters() != null)
            resource.uriParameters().stream()
                    .filter(p -> !parameters.containsKey(p.name().toUpperCase()))
                    .forEach(p -> parameters.put(p.name().toUpperCase(), new V08_RamlParameter(p)));

        Optional.ofNullable(resource.parentResource()).ifPresent(r -> addParameters(r, parameters));
    }
}
