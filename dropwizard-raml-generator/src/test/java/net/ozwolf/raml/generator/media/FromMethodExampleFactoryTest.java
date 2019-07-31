package net.ozwolf.raml.generator.media;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.ozwolf.raml.generator.media.json.JsonTestResponse;
import net.ozwolf.raml.generator.media.xml.XmlTestResponse;
import net.ozwolf.raml.generator.util.TestMappers;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.Map;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class FromMethodExampleFactoryTest {
    private final static ObjectMapper XML_MAPPER = new XmlMapper();

    @Test
    public void shouldCreateJsonExample() throws JSONException {
        String example = new FromMethodExampleFactory(TestMappers.json()).create(JsonTestResponse.class, false).orElse(null);

        JSONAssert.assertEquals(fixture("fixtures/media/json/example.json"), example, true);
    }

    @Test
    public void shouldCreateJsonArrayExample() throws JSONException {
        String example = new FromMethodExampleFactory(TestMappers.json()).create(JsonTestResponse.class, true).orElse(null);

        JSONAssert.assertEquals(fixture("fixtures/media/json/array-example.json"), example, true);
    }

    @Test
    public void shouldCreateXmlExample() throws IOException {
        String example = new FromMethodExampleFactory(TestMappers.xml()).create(XmlTestResponse.class, false).orElse(null);

        Map<String, Object> expected = XML_MAPPER.readValue(fixture("fixtures/media/xml/example.xml"), new JacksonDocument());
        Map<String, Object> actual = XML_MAPPER.readValue(example, new JacksonDocument());

        assertThat(actual).isEqualTo(expected);
    }

    private static class JacksonDocument extends TypeReference<Map<String, Object>> {
    }
}