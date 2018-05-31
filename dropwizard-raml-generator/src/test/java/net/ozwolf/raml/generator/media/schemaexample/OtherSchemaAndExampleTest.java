package net.ozwolf.raml.generator.media.schemaexample;

import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.generator.model.SchemaAndExample;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OtherSchemaAndExampleTest {
    @Test
    void shouldAlwaysReturnNoneForAnAnnotation(){
        SchemaAndExample result = new OtherSchemaAndExample().generate((RamlBody) null);

        assertThat(result).isEqualTo(SchemaAndExample.NONE);
    }

    @Test
    void shouldAlwaysReturnNoneForAnType(){
        SchemaAndExample result = new OtherSchemaAndExample().generate((Class<?>) null);

        assertThat(result).isEqualTo(SchemaAndExample.NONE);
    }
}