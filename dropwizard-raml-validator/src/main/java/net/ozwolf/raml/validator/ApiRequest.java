package net.ozwolf.raml.validator;

import net.ozwolf.raml.common.model.*;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static net.ozwolf.raml.validator.util.ListUtils.getOrEmpty;

public class ApiRequest {
    private final RamlResource resource;
    private final RamlMethod method;
    private final String path;
    private final MediaType mediaType;
    private final Map<String, List<String>> headers;
    private final Map<String, List<String>> queryParameters;
    private final Map<String, List<String>> pathParameters;
    private final byte[] content;

    private ApiRequest(RamlResource resource,
                       RamlMethod method,
                       String path,
                       MediaType mediaType,
                       Map<String, List<String>> headers,
                       Map<String, List<String>> queryParameters,
                       Map<String, List<String>> pathParameters,
                       byte[] content) {
        this.resource = resource;
        this.method = method;
        this.path = path;
        this.mediaType = mediaType;
        this.headers = headers;
        this.queryParameters = queryParameters;
        this.pathParameters = pathParameters;
        this.content = content;
    }

    public String getMethod() {
        return method.getMethod();
    }

    public String getPath() {
        return path;
    }

    public String getMediaType() {
        return mediaType == null ? null : mediaType.toString();
    }

    public Optional<byte[]> getContent() {
        return Optional.ofNullable(content);
    }

    public List<RamlSecurity> getSecuritySpecifications() {
        return method.getSecurity();
    }

    public Optional<RamlBody> getBodySpecification() {
        return method.findRequestBodyFor(mediaType);
    }

    public List<RamlParameter> getHeaderSpecifications() {
        return method.getHeaders();
    }

    public List<RamlParameter> getUriParameterSpecifications() {
        return resource.getUriParameters();
    }

    public List<RamlParameter> getQueryParameterSpecifications() {
        return method.getQueryParameters();
    }

    public List<String> getActualHeader(String name) {
        return getOrEmpty(headers.get(name));
    }

    public List<String> getActualQueryParameter(String name) {
        return getOrEmpty(queryParameters.get(name));
    }

    public List<String> getActualPathParameter(String name) {
        return getOrEmpty(pathParameters.get(name));
    }

    public boolean hasSecurity() {
        return !method.getSecurity().isEmpty();
    }

    public List<RamlSecurity> getActiveSecurity() {
        return method.getSecurity()
                .stream()
                .filter(s -> {
                    boolean headerPresent = s.getHeaders().stream().anyMatch(h -> !getActualHeader(h.getName()).isEmpty());
                    boolean queryParameterPresent = s.getQueryParameters().stream().anyMatch(q -> !getActualPathParameter(q.getName()).isEmpty());
                    return headerPresent || queryParameterPresent;
                })
                .collect(toList());
    }

    public static class Builder {
        private final RamlResource resource;
        private final RamlMethod method;
        private final String path;
        private final MediaType mediaType;

        private Map<String, List<String>> headers;
        private Map<String, List<String>> queryParameters;
        private Map<String, List<String>> pathParameters;
        private byte[] content;

        public Builder(RamlResource resource, RamlMethod method, String path, MediaType mediaType) {
            this.resource = resource;
            this.method = method;
            this.path = path;
            this.mediaType = mediaType;

            this.headers = new HashMap<>();
            this.queryParameters = new HashMap<>();
            this.pathParameters = new HashMap<>();
            this.content = null;
        }

        public Builder withHeaders(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withQueryParameters(Map<String, List<String>> parameters) {
            this.queryParameters = parameters;
            return this;
        }

        public Builder withPathParameters(Map<String, List<String>> parameters) {
            this.pathParameters = parameters;
            return this;
        }

        public Builder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        public ApiRequest build() {
            return new ApiRequest(
                    resource,
                    method,
                    path,
                    mediaType,
                    headers,
                    queryParameters,
                    pathParameters,
                    content
            );
        }
    }
}
