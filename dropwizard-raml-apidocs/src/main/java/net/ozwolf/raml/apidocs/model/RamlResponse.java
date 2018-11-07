package net.ozwolf.raml.apidocs.model;

import net.ozwolf.raml.apidocs.util.MarkDownHelper;

import javax.ws.rs.core.Response;
import java.util.List;

public interface RamlResponse {
    Integer getStatus();

    default String getStatusName() {
        Response.Status status = Response.Status.fromStatusCode(getStatus());

        if (status != null)
            return status.getReasonPhrase();

        switch (getStatus()) {
            case 422:
                return "Unprocessible Entity";
            case 423:
                return "Locked";
            default:
                return "";
        }
    }

    String getDescription();

    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    List<RamlParameter> getHeaders();

    List<RamlBody> getBodies();
}
