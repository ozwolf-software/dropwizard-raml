package net.ozwolf.raml.generator.media.json;

import net.ozwolf.raml.generator.util.TestMappers;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;

class JsonSchemaFactoryTest {
    @Test
    void shouldCreateSchema() throws JSONException {
        String schema = new JsonSchemaFactory().create(JsonTestResponse.class, TestMappers.json()).orElse(null);
        JSONAssert.assertEquals(fixture("fixtures/media/json/schema.json"), schema, true);
    }
}