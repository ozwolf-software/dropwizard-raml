package net.ozwolf.raml.common.model.v10;

import net.ozwolf.raml.common.model.RamlBody;
import org.raml.v2.api.model.v10.datamodel.ExampleSpec;
import org.raml.v2.api.model.v10.datamodel.ExternalTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import java.util.Optional;

/**
 * <h1>RAML 1.0 Body</h1>
 *
 * A 1.0 representation of a body specification
 */
public class V10_RamlBody implements RamlBody {
    private final TypeDeclaration body;

    /**
     * Create a new 1.0 body specification
     *
     * @param body the 1.0 specification
     */
    public V10_RamlBody(TypeDeclaration body) {
        this.body = body;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getContentType() {
        return body.name();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getSchema() {
        return (body instanceof ExternalTypeDeclaration) ? ((ExternalTypeDeclaration) body).schemaContent() : null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getExample() {
        return Optional.ofNullable(body.example()).map(ExampleSpec::value).orElse(null);
    }
}
