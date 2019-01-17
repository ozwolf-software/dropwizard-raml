package net.ozwolf.raml.common.model.v10;

import net.ozwolf.raml.common.model.RamlApplication;
import net.ozwolf.raml.common.model.RamlDocumentation;
import net.ozwolf.raml.common.model.RamlResource;
import org.raml.v2.api.model.v10.api.Api;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

/**
 * <h1>RAML 1.0 Application</h1>
 *
 * This is the application representation for a RAML 1.0 specification
 */
public class V10_RamlApplication implements RamlApplication {
    private final Api api;

    /**
     * Create a new RAML Application using a 1.0 API specification
     *
     * @param api the 1.0 API specification
     */
    public V10_RamlApplication(Api api) {
        this.api = api;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getTitle() {
        return Optional.ofNullable(api.title()).map(AnnotableStringType::value).orElse("Unknown");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getVersion() {
        return Optional.ofNullable(api.version()).map(AnnotableStringType::value).orElse("Unknown");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(api.description()).map(AnnotableStringType::value).orElse(null);
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
        return Optional.ofNullable(api.baseUri()).map(AnnotableStringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlDocumentation> getDocumentation() {
        return api.documentation().stream().map(V10_RamlDocumentation::new).collect(toList());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<RamlResource> getResources() {
        return api.resources().stream().map(V10_RamlResource::new).collect(toList());
    }
}
