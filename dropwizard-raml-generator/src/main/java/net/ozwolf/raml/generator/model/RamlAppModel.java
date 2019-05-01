package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.*;

import java.lang.annotation.Annotation;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

@JsonSerialize
@JsonPropertyOrder({"title", "description", "version", "protocols", "baseUri", "documentation", "securitySchemes", "traits"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlAppModel {
    private final String title;
    private final String description;
    private final String version;
    private final Set<String> protocols;
    private final String baseUri;
    private final List<RamlDocumentationModel> documentation;
    private final Map<String, RamlSecurityModel> securitySchemes;
    private final Map<String, RamlDescribedByModel> traits;
    private final Map<Integer, RamlResponseModel> globalResponses;

    private final List<RamlResourceModel> resources;

    public RamlAppModel(String version,
                        RamlApp annotation) {
        this.title = annotation.title();
        this.description = annotation.description();
        this.version = version;
        this.protocols = newHashSet(annotation.protocols());
        this.baseUri = annotation.baseUri();
        this.documentation = Arrays.stream(annotation.documentation()).map(RamlDocumentationModel::new).collect(toList());
        this.securitySchemes = Arrays.stream(annotation.security()).collect(toMap(RamlSecurityScheme::key, RamlSecurityModel::new));
        this.traits = Arrays.stream(annotation.traits()).collect(toMap(RamlTrait::key, a -> new RamlDescribedByModel(a.describedBy())));
        this.traits.put("deprecated", deprecatedModel());
        this.globalResponses = Arrays.stream(annotation.globalResponses()).collect(toMap(RamlResponse::status, RamlResponseModel::new));

        this.resources = newArrayList();
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return trimToNull(description);
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("protocols")
    public Set<String> getProtocols() {
        return protocols;
    }

    @JsonProperty("baseUri")
    public String getBaseUri() {
        return baseUri;
    }

    @JsonProperty("documentation")
    public List<RamlDocumentationModel> getDocumentation() {
        return nullIfEmpty(documentation);
    }

    @JsonProperty("securitySchemes")
    public Map<String, RamlSecurityModel> getSecuritySchemes() {
        return nullIfEmpty(securitySchemes);
    }

    @JsonProperty("traits")
    public Map<String, RamlDescribedByModel> getTraits() {
        return nullIfEmpty(traits);
    }

    @JsonIgnore
    public Map<Integer, RamlResponseModel> getGlobalResponses() {
        return globalResponses;
    }

    @JsonAnyGetter
    public Map<String, RamlResourceModel> getResources() {
        Map<String, RamlResourceModel> result = newLinkedHashMap();

        this.resources.stream()
                .sorted(Comparator.comparingInt(RamlResourceModel::getDisplayOrder).thenComparing(RamlResourceModel::getDisplayName))
                .forEach(r -> result.put(r.getPath(), r));

        return result;
    }

    public void addResource(RamlResourceModel model) {
        this.resources.add(model);
    }

    private static RamlDescribedByModel deprecatedModel(){
        return new RamlDescribedByModel(
                new RamlDescribedBy(){
                    @Override
                    public RamlParameter[] headers() {
                        return new RamlParameter[0];
                    }

                    @Override
                    public RamlParameter[] queryParameters() {
                        return new RamlParameter[0];
                    }

                    @Override
                    public RamlResponse[] responses() {
                        return new RamlResponse[0];
                    }

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return RamlDescribedBy.class;
                    }
                }
        );
    }
}
