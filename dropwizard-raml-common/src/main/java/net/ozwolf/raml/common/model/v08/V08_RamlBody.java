package net.ozwolf.raml.common.model.v08;

import net.ozwolf.raml.common.model.RamlBody;
import org.raml.v2.api.model.v08.bodies.BodyLike;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.Optional;

/**
 * <h1>RAML 0.8 Body</h1>
 *
 * A 0.8 definition of a body element
 */
public class V08_RamlBody implements RamlBody {
    private final BodyLike body;

    /**
     * Create a new 0.8 body item
     *
     * @param body the body
     */
    public V08_RamlBody(BodyLike body) {
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
        return body.schemaContent();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getExample() {
        return Optional.ofNullable(body.example()).map(StringType::value).orElse(null);
    }
}
