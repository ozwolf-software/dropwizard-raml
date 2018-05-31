package net.ozwolf.raml.generator.media.schemaexample;

import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.generator.media.SchemaAndExampleGenerator;
import net.ozwolf.raml.generator.model.SchemaAndExample;

public class OtherSchemaAndExample implements SchemaAndExampleGenerator {
    @Override
    public SchemaAndExample generate(RamlBody annotation) {
        return SchemaAndExample.NONE;
    }

    @Override
    public SchemaAndExample generate(Class<?> type) {
        return SchemaAndExample.NONE;
    }
}
