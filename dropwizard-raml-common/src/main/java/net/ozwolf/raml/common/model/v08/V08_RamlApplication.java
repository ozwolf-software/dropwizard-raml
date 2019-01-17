package net.ozwolf.raml.common.model.v08;

import net.ozwolf.raml.common.model.RamlApplication;
import net.ozwolf.raml.common.model.RamlDocumentation;
import net.ozwolf.raml.common.model.RamlResource;
import org.raml.v2.api.model.v08.api.Api;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 0.8 Application</h1>
 *
 * A 0.8 definition of an application specification
 */
public class V08_RamlApplication implements RamlApplication {
    private final Api api;

    /**
     * Create a new 0.8 application
     *
     * @param api the application API
     */
    public V08_RamlApplication(Api api) {
        this.api = api;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getTitle() {
        return api.title();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getVersion() {
        return api.version();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(api.description()).map(StringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Set<String> getProtocols() {
        return newHashSet(api.protocols());
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getBaseUri() {
        return Optional.ofNullable(api.baseUri()).map(StringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlDocumentation> getDocumentation() {
        return api.documentation().stream().map(V08_RamlDocumentation::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlResource> getResources() {
        return api.resources().stream().map(V08_RamlResource::new).collect(toList());
    }
}
