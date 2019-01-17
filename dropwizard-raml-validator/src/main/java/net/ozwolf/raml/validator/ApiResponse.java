package net.ozwolf.raml.validator;

import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.common.model.RamlMethod;
import net.ozwolf.raml.common.model.RamlResponse;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.ozwolf.raml.validator.util.ListUtils.getOrEmpty;

public class ApiResponse {
    private final RamlMethod method;
    private final Integer status;
    private final MediaType mediaType;
    private final Map<String, List<String>> headers;
    private final byte[] content;

    private ApiResponse(RamlMethod method,
                        Integer status,
                        MediaType mediaType,
                        Map<String, List<String>> headers,
                        byte[] content) {
        this.method = method;
        this.status = status;
        this.mediaType = mediaType;
        this.headers = headers;
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public Optional<byte[]> getContent() {
        return Optional.ofNullable(content);
    }

    public Optional<RamlResponse> getResponseSpecification() {
        return method.findResponseFor(status);
    }

    public Optional<RamlBody> getBodySpecification() {
        return getResponseSpecification().flatMap(r -> r.findBodyFor(mediaType));
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public List<String> getActualHeader(String name) {
        return getOrEmpty(headers.get(name));
    }

    public boolean isClientError() {
        return this.status >= 400 && this.status <= 499;
    }

    public static class Builder {
        private final RamlMethod method;
        private final Integer status;
        private final MediaType mediaType;

        private Map<String, List<String>> headers;
        private byte[] content;

        public Builder(RamlMethod method, Integer status, MediaType mediaType) {
            this.method = method;
            this.status = status;
            this.mediaType = mediaType;

            this.headers = new HashMap<>();
            this.content = null;
        }

        public Builder withHeaders(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(
                    method,
                    status,
                    mediaType,
                    headers,
                    content
            );
        }
    }
}
