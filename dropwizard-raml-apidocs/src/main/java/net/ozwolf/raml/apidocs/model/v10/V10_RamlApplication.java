package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlApplication;
import net.ozwolf.raml.apidocs.model.RamlDocumentation;
import net.ozwolf.raml.apidocs.model.RamlResource;
import org.raml.v2.api.model.v10.api.Api;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

public class V10_RamlApplication implements RamlApplication {
    private final Api api;

    public V10_RamlApplication(Api api) {
        this.api = api;
    }

    @Override
    public String getTitle() {
        return Optional.ofNullable(api.title()).map(AnnotableStringType::value).orElse("Unknown");
    }

    @Override
    public String getVersion() {
        return Optional.ofNullable(api.version()).map(AnnotableStringType::value).orElse("Unknown");
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(api.description()).map(AnnotableStringType::value).orElse(null);
    }

    @Override
    public Set<String> getProtocols() {
        return newHashSet(api.protocols());
    }

    @Override
    public String getBaseUri() {
        return Optional.ofNullable(api.baseUri()).map(AnnotableStringType::value).orElse(null);
    }

    @Override
    public List<RamlDocumentation> getDocumentation() {
        return api.documentation().stream().map(V10_RamlDocumentation::new).collect(toList());
    }

    @Override
    public List<RamlResource> getResources() {
        return api.resources().stream().map(V10_RamlResource::new).collect(toList());
    }
}
