package net.ozwolf.raml.monitor.managed;

import net.ozwolf.raml.validator.ApiRequest;
import net.ozwolf.raml.validator.ApiResponse;

public class RamlMonitorScope {
    private final ApiRequest request;
    private ApiResponse.Builder response;

    RamlMonitorScope(ApiRequest request) {
        this.request = request;
    }

    public void withResponse(ApiResponse.Builder builder) {
        this.response = builder;
    }

    public ApiRequest getRequest() {
        return request;
    }

    public ApiResponse.Builder getResponse() {
        return response;
    }
}
