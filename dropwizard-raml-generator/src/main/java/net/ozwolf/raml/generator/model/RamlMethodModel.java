package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

@JsonSerialize
@JsonPropertyOrder({"description", "securedBy", "is", "responses"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlMethodModel {
    private final String action;
    private final String description;
    private final Set<String> securedBy;
    private final Set<String> traits;
    private final Map<Integer, RamlResponseModel> responses;

    public RamlMethodModel(String action, Method method) {
        this.action = action;
        this.description = Optional.ofNullable(method.getAnnotation(RamlDescription.class)).map(RamlDescription::value).orElse(null);
        this.securedBy = Optional.ofNullable(method.getAnnotation(RamlSecuredBy.class)).map(v -> newHashSet(v.value())).orElse(null);
        this.traits = Optional.ofNullable(method.getAnnotation(RamlTraits.class)).map(v -> newHashSet(v.value())).orElse(null);
        this.responses = getResponses(method);
    }

    @JsonIgnore
    public String getAction() {
        return action;
    }

    @JsonProperty("description")
    public String getDescription() {
        return trimToNull(description);
    }

    @JsonProperty("securedBy")
    public Set<String> getSecuredBy() {
        return nullIfEmpty(securedBy);
    }

    @JsonProperty("traits")
    public Set<String> getTraits() {
        return nullIfEmpty(traits);
    }

    @JsonProperty("responses")
    public Map<Integer, RamlResponseModel> getResponses() {
        return responses;
    }

    private static Map<Integer, RamlResponseModel> getResponses(Method method) {
        RamlResponses annotation = method.getAnnotation(RamlResponses.class);
        if (annotation != null)
            return Arrays.stream(annotation.value()).collect(toMap(RamlResponse::status, RamlResponseModel::new));

        Set<String> contentTypes = Optional.ofNullable(method.getAnnotation(Produces.class)).map(v -> newHashSet(v.value())).orElse(newHashSet("application/json"));

        Class<?> returnType = method.getReturnType();
        Map<Integer, RamlResponseModel> responses = newHashMap();

        if (returnType == Response.class) {
            responses.put(200, new RamlResponseModel(contentTypes));
        } else {
            responses.put(200, new RamlResponseModel(contentTypes, returnType));
        }

        return responses;
    }
}
