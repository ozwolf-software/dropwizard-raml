package net.ozwolf.raml.generator.media.json;

import net.ozwolf.raml.generator.util.TestMappers;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;

public class JsonSchemaFactoryTest {
    @Test
    public void shouldCreateSchema() throws JSONException {
        String schema = new JsonSchemaFactory().create(JsonTestResponse.class, false, TestMappers.json()).orElse(null);
        JSONAssert.assertEquals(fixture("fixtures/media/json/schema.json"), schema, true);
    }

    @Test
    public void shouldCreateArraySchema() throws JSONException {
        String schema = new JsonSchemaFactory().create(JsonTestResponse.class, true, TestMappers.json()).orElse(null);
        JSONAssert.assertEquals(fixture("fixtures/media/json/array-schema.json"), schema, true);
    }
}