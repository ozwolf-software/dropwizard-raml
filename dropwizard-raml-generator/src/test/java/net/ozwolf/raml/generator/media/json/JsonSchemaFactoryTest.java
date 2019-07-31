package net.ozwolf.raml.generator.media.json;

import net.ozwolf.raml.generator.util.TestMappers;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;

public class JsonSchemaFactoryTest {
    @Test
    public void shouldCreateSchema() throws JSONException {
        String schema = new JsonSchemaFactory(TestMappers.json()).create(JsonTestResponse.class, false).orElse(null);
        JSONAssert.assertEquals(fixture("fixtures/media/json/schema.json"), schema, true);
    }

    @Test
    public void shouldCreateArraySchema() throws JSONException {
        String schema = new JsonSchemaFactory(TestMappers.json()).create(JsonTestResponse.class, true).orElse(null);
        JSONAssert.assertEquals(fixture("fixtures/media/json/array-schema.json"), schema, true);
    }
}