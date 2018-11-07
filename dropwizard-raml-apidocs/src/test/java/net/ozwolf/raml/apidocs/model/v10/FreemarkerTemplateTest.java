package net.ozwolf.raml.apidocs.model.v10;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.ozwolf.raml.apidocs.model.RamlApplication;
import net.ozwolf.raml.generator.RamlGenerator;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import org.junit.Test;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.api.Assertions.assertThat;

public class FreemarkerTemplateTest {
    @Test
    public void shouldGenerateRamlApplication() throws RamlGenerationException, IOException, TemplateException {
        RamlGenerator generator = new RamlGenerator("net.ozwolf.raml.test", "1.2.3");

        String raml = generator.generate();

        RamlModelResult result = new RamlModelBuilder().buildApi(raml, "/");

        RamlApplication app = new V10_RamlApplication(result.getApiV10());

        Map<String, Object> data = newHashMap();
        data.put("application", app);

        Configuration c = new Configuration(Configuration.VERSION_2_3_27);
        c.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        c.setDefaultEncoding("UTF-8");

        Template t = c.getTemplate("index.ftl");

        StringWriter writer = new StringWriter();
        t.process(data, writer);

        String html = writer.toString();

        assertThat(html).contains("<h3>Books</h3>");
        assertThat(html).contains("<h3>Authors</h3>");
    }
}