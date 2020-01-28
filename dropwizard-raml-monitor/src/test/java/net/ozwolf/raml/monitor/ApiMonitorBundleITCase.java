package net.ozwolf.raml.monitor;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.ozwolf.raml.test.TestServiceApp;
import net.ozwolf.raml.test.TestServiceAppConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Maps.newHashMap;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class ApiMonitorBundleITCase {
    @ClassRule
    public final static DropwizardAppRule<TestServiceAppConfiguration> APP = new DropwizardAppRule<TestServiceAppConfiguration>(
            ApiMonitorApp.class,
            "src/test/resources/fixtures/configuration.yml"
    );

    private final static File RAML_MONITOR_LOG = new File("target/logs/raml-monitor.log");

    @Before
    public void setUp() throws IOException {
        FileUtils.writeStringToFile(RAML_MONITOR_LOG, "", false);
    }

    @Test
    public void shouldMonitorRequestsToTheService() throws InterruptedException {
        shouldPassValidationProcess();
        shouldNotLogValidationErrorDueToClientResponse();
        shouldLogErrorAsSuccessfulRequestAndResponseDidNotMeetSpecificationRequirements();
    }

    private void shouldPassValidationProcess() {
        Response response = APP.client().target(uriBuilder().path("/books").build())
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(200);

        response.close();
    }

    private void shouldNotLogValidationErrorDueToClientResponse() {
        Response response = APP.client().target(uriBuilder().path("/books").build())
                .request()
                .post(Entity.json("{}"));

        assertThat(response.getStatus()).isEqualTo(422);

        response.close();
    }

    private void shouldLogErrorAsSuccessfulRequestAndResponseDidNotMeetSpecificationRequirements() throws InterruptedException {
        Response response = APP.client().target(uriBuilder().path("/books").build())
                .request()
                .header(HttpHeaders.AUTHORIZATION, "something-wrong")
                .post(Entity.json(bookRequest()));

        assertThat(response.getStatus()).isEqualTo(201);

        TimeUnit.SECONDS.sleep(5);

        verifyViolationSegmentFor("fixtures/validation/post-books-violations.txt");
        response.close();
    }

    private static Map<String, Object> bookRequest() {
        Map<String, Object> request = newHashMap();
        request.put("title", "Test Book");
        request.put("genre", "SciFi");
        request.put("publishDate", "2018-12-20");
        request.put("authorId", 1);
        request.put("rrp", "19.99");
        return request;
    }

    private static UriBuilder uriBuilder() {
        return UriBuilder.fromPath("/").scheme("http").host("localhost").port(9443);
    }

    private static void verifyViolationSegmentFor(String fixture) {
        try {
            String content = FileUtils.readFileToString(RAML_MONITOR_LOG);
            String expected = fixture(fixture).replaceAll("\r\n", "\n");
            assertThat(content).contains(expected);
        } catch (IOException e) {
            fail("Failed to read log file.", e);
        }
    }

    public static class ApiMonitorApp extends TestServiceApp {
        @Override
        public void initialize(Bootstrap<TestServiceAppConfiguration> bootstrap) {
            bootstrap.addBundle(new ApiMonitorBundle("net.ozwolf.raml.test", "1.2.3"));
            super.initialize(bootstrap);
        }
    }
}