package net.ozwolf.raml.apidocs.model.v08;

import net.ozwolf.raml.apidocs.model.RamlBody;
import org.raml.v2.api.model.v08.bodies.BodyLike;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.Optional;

public class V08_RamlBody implements RamlBody {
    private final BodyLike body;

    public V08_RamlBody(BodyLike body) {
        this.body = body;
    }

    @Override
    public String getContentType() {
        return body.name();
    }

    @Override
    public String getSchema() {
        return body.schemaContent();
    }

    @Override
    public String getExample() {
        return Optional.ofNullable(body.example()).map(StringType::value).orElse(null);
    }
}
