package net.ozwolf.raml.generator.media.json;

import net.ozwolf.raml.generator.util.TestMappers;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;

class JsonExampleFactoryTest {
    @Test
    void shouldCreateExample() throws JSONException {
        String example = new JsonExampleFactory().create(JsonTestResponse.class, TestMappers.json()).orElse(null);

        JSONAssert.assertEquals(fixture("fixtures/json-schema-example/example.json"), example, true);
    }
}