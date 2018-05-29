package net.ozwolf.raml.generator.media;

import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.generator.model.SchemaAndExample;

public interface SchemaAndExampleGenerator {
    SchemaAndExample generate(RamlBody annotation);
    SchemaAndExample generate(Class<?> type);
}
