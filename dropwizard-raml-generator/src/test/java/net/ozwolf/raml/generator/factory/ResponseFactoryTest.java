package net.ozwolf.raml.generator.factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlBody;
import net.ozwolf.raml.annotations.RamlResponse;
import net.ozwolf.raml.annotations.RamlResponses;
import net.ozwolf.raml.generator.model.RamlResponseModel;
import org.junit.Test;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.api.Assertions.assertThat;

public class ResponseFactoryTest {
    @Test
    public void shouldGenerateResponsesBasedOnAnnotation() throws NoSuchMethodException {
        Method method = ResponseFactoryTest.class.getMethod("getAnnotatedMethod");

        Map<Integer, RamlResponseModel> result = newHashMap();
        new ResponseFactory().getResponses(method, m -> result.put(m.getStatus(), m), null);

        assertThat(result)
                .hasSize(2)
                .containsKeys(200, 404);

        RamlResponseModel ok = result.get(200);
        assertThat(ok.getDescription()).isEqualTo("ok");
    }

    @Test
    public void shouldGenerateDefaultResponseBasedOnMethod() throws NoSuchMethodException {
        Method method = ResponseFactoryTest.class.getMethod("getResponseMethod");

        Map<Integer, RamlResponseModel> result = newHashMap();
        new ResponseFactory().getResponses(method, m -> result.put(m.getStatus(), m), null);

        assertThat(result)
                .hasSize(1)
                .containsKeys(200);

        RamlResponseModel ok = result.get(200);
        assertThat(ok.getBody())
                .hasSize(1)
                .containsKeys("application/json");
    }

    @Test
    public void shouldGenerateResponseFromType() throws NoSuchMethodException {
            Method method = ResponseFactoryTest.class.getMethod("getTypeMethod");

            Map<Integer, RamlResponseModel> result = newHashMap();
            new ResponseFactory().getResponses(method, m -> result.put(m.getStatus(), m), null);

            assertThat(result)
                    .hasSize(1)
                    .containsKeys(200);

            RamlResponseModel ok = result.get(200);
            assertThat(ok.getBody())
                    .hasSize(1)
                    .containsKeys("application/json");

            assertThat(ok.getBody().get("application/json").getSchema()).contains("name");
    }

    @RamlResponses({
            @RamlResponse(
                    status = 200,
                    description = "ok",
                    bodies = @RamlBody(contentType = "text/plain", example = "ok go")
            ),
            @RamlResponse(
                    status = 404,
                    description = "not found",
                    bodies = @RamlBody(contentType = "text/plain", example = "not found")
            )
    })
    public String getAnnotatedMethod() {
        return "ok";
    }

    @Produces("application/json")
    public Response getResponseMethod(){
        return Response.ok().build();
    }

    @Produces("application/json")
    public ResponseBody getTypeMethod(){
        return new ResponseBody();
    }

    @JsonSerialize
    @JsonPropertyOrder("name")
    public static class ResponseBody {
        private final String name;

        public ResponseBody(){
            this.name = "test";
        }

        @JsonProperty(value = "name", required = true)
        public String getName() {
            return name;
        }
    }
}