package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.annotations.RamlRequests;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.model.RamlBodyModel;
import org.junit.jupiter.api.Test;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static net.ozwolf.raml.generator.matchers.RamlGeneratorErrorMatchers.errorOf;
import static org.assertj.core.api.Assertions.assertThat;

class RequestFactoryTest {
    @Test
    void shouldGenerateRequestBodiesFromAnnotation() throws NoSuchMethodException {
        Method method = RequestFactoryTest.class.getMethod("putValidRequest", String.class);

        Map<String, RamlBodyModel> result = newHashMap();

        RequestFactory factory = new RequestFactory();

        factory.getRequests(method, m -> result.put(m.getContentType(), m), null);

        assertThat(result)
                .hasSize(1)
                .containsKeys("text/plain");

        assertThat(result.get("text/plain").getExample()).isEqualTo("example body");
    }

    @Test
    void shouldHandleMethodGracefullyIfNotExpectedToHaveBody() throws NoSuchMethodException {
        Method method = RequestFactoryTest.class.getMethod("getValidResponse", String.class);

        RequestFactory factory = new RequestFactory();

        // No errors should occur (ie. null pointer due to no handlers)
        factory.getRequests(method, null, null);
    }

    @Test
    void shouldHandleMethodWithNoDetectedBody() throws NoSuchMethodException {
        Method method = RequestFactoryTest.class.getMethod("postValidRequest");

        RequestFactory factory = new RequestFactory();

        // No errors should occur (ie. null pointer due to no handlers)
        factory.getRequests(method, null, null);
    }

    @Test
    void shouldRaiseErrorIfRequestBodyDetectedButNoRamlRequestsDefined() throws NoSuchMethodException {
        Method method = RequestFactoryTest.class.getMethod("putInvalidRequest", String.class);

        RequestFactory factory = new RequestFactory();

        List<RamlGenerationError> errors = newArrayList();
        factory.getRequests(method, null, errors::add);

        assertThat(errors)
                .hasSize(1)
                .areAtLeastOne(errorOf("RequestFactoryTest.putInvalidRequest : request body entity found but missing [ @" + RamlRequests.class.getSimpleName() + " ] annotation"));
    }

    @RamlRequests(
            @RamlBody(contentType = "text/plain", example = "example body")
    )
    @PUT
    public Response putValidRequest(String request) {
        return Response.ok().build();
    }

    @PUT
    public Response putInvalidRequest(String request) {
        return Response.ok().build();
    }

    @GET
    public Response getValidResponse(String request) {
        return Response.ok().build();
    }

    @POST
    public Response postValidRequest(){
        return Response.ok().build();
    }
}