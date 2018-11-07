package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlBody;
import org.raml.v2.api.model.v10.datamodel.ExampleSpec;
import org.raml.v2.api.model.v10.datamodel.ExternalTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import java.util.Optional;

public class V10_RamlBody implements RamlBody {
    private final TypeDeclaration body;

    public V10_RamlBody(TypeDeclaration body) {
        this.body = body;
    }

    @Override
    public String getContentType() {
        return body.name();
    }

    @Override
    public String getSchema() {
        return (body instanceof ExternalTypeDeclaration) ? ((ExternalTypeDeclaration) body).schemaContent() : null;
    }

    @Override
    public String getExample() {
        return Optional.ofNullable(body.example()).map(ExampleSpec::value).orElse(null);
    }
}
