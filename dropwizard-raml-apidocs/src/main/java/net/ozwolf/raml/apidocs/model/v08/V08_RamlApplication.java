package net.ozwolf.raml.apidocs.model.v08;

import net.ozwolf.raml.apidocs.model.RamlApplication;
import net.ozwolf.raml.apidocs.model.RamlDocumentation;
import net.ozwolf.raml.apidocs.model.RamlResource;
import org.raml.v2.api.model.v08.api.Api;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

public class V08_RamlApplication implements RamlApplication {
    private final Api api;

    public V08_RamlApplication(Api api) {
        this.api = api;
    }

    @Override
    public String getTitle() {
        return api.title();
    }

    @Override
    public String getVersion() {
        return api.version();
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(api.description()).map(StringType::value).orElse(null);
    }

    @Override
    public Set<String> getProtocols() {
        return newHashSet(api.protocols());
    }

    @Override
    public String getBaseUri() {
        return Optional.ofNullable(api.baseUri()).map(StringType::value).orElse(null);
    }

    @Override
    public List<RamlDocumentation> getDocumentation() {
        return api.documentation().stream().map(V08_RamlDocumentation::new).collect(toList());
    }

    @Override
    public List<RamlResource> getResources() {
        return api.resources().stream().map(V08_RamlResource::new).collect(toList());
    }
}
