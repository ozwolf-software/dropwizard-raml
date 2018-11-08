package net.ozwolf.raml.apidocs;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.ozwolf.raml.test.TestServiceApp;
import net.ozwolf.raml.test.TestServiceAppConfiguration;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiDocsBundleITCase {
    @ClassRule
    public final static DropwizardAppRule<TestServiceAppConfiguration> APP = new DropwizardAppRule<TestServiceAppConfiguration>(
            ApiDocsApp.class,
            "src/test/resources/fixtures/configuration.yml"
    );

    @Test
    public void shouldAddApiDocsViewUnderOperationsPort() {
        URI resource = UriBuilder.fromPath("/apidocs").scheme("http").host("localhost").port(8443).build();
        String apidocs = APP.client().target(resource)
                .request()
                .get(String.class);

        assertThat(apidocs).contains("<h3>Books</h3>");
        assertThat(apidocs).contains("<h3>Authors</h3>");
    }

    public static class ApiDocsApp extends TestServiceApp {
        @Override
        public void initialize(Bootstrap<TestServiceAppConfiguration> bootstrap) {
            bootstrap.addBundle(new ApiDocsBundle("net.ozwolf.raml.test", "1.2.3"));
            super.initialize(bootstrap);
        }
    }
}