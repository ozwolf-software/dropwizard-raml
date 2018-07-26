package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlApplication;
import net.ozwolf.raml.apidocs.model.RamlResource;
import org.raml.v2.api.model.v10.api.Api;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

public class V10_RamlApplication implements RamlApplication {
    private final Api api;

    public V10_RamlApplication(Api api) {
        this.api = api;
    }

    @Override
    public String getTitle() {
        return api.title().value();
    }

    @Override
    public String getVersion() {
        return api.version().value();
    }

    @Override
    public String getDescription() {
        return api.description().toString();
    }

    @Override
    public Set<String> getProtocols() {
        return newHashSet(api.protocols());
    }

    @Override
    public List<RamlResource> getResources() {
        return api.resources().stream().map(V10_RamlResource::new).collect(toList());
    }
}
