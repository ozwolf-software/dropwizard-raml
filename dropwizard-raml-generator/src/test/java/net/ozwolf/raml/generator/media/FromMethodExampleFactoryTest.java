package net.ozwolf.raml.generator.media;

import net.ozwolf.raml.generator.media.json.JsonTestResponse;
import net.ozwolf.raml.generator.media.xml.XmlTestResponse;
import net.ozwolf.raml.generator.util.TestMappers;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;

class FromMethodExampleFactoryTest {
    @Test
    void shouldCreateJsonExample() throws JSONException {
        String example = new FromMethodExampleFactory().create(JsonTestResponse.class, TestMappers.json()).orElse(null);

        JSONAssert.assertEquals(fixture("fixtures/media/json/example.json"), example, true);
    }

    @Test
    void shouldCreateXmlExample()  {
        String example = new FromMethodExampleFactory().create(XmlTestResponse.class, TestMappers.xml()).orElse(null);

        System.out.println(example);
    }
}