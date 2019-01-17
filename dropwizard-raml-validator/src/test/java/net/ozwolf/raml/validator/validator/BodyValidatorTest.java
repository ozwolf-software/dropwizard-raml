package net.ozwolf.raml.validator.validator;

import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import net.ozwolf.raml.validator.media.MediaValidator;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static net.ozwolf.raml.validator.testutil.SpecificationViolationConditions.violationOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BodyValidatorTest {
    private final MediaValidator mediaValidator = mock(MediaValidator.class);

    @Test
    public void shouldPassValidationForMediaType() {
        Node node = new Node("body");
        String schema = "schema";
        RamlBody body = mock(RamlBody.class);
        when(body.getContentType()).thenReturn("application/json");
        when(body.getSchema()).thenReturn(schema);
        String content = "content";

        when(mediaValidator.validate(node.createChild("application/json"), schema, content.getBytes())).thenReturn(newArrayList());

        BodyValidator validator = new BodyValidator(mediaValidators());

        List<SpecificationViolation> result = validator.validate(node, body, content.getBytes());

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldPassValidationWhenNoSchemaPresent() {
        Node node = new Node("body");
        RamlBody body = mock(RamlBody.class);
        when(body.getContentType()).thenReturn("application/json");
        when(body.getSchema()).thenReturn("");
        String content = "content";

        BodyValidator validator = new BodyValidator(mediaValidators());

        List<SpecificationViolation> result = validator.validate(node, body, content.getBytes());

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldPassValidationWhenNoMediaValidatorAvailable() {
        Node node = new Node("body");
        RamlBody body = mock(RamlBody.class);
        when(body.getContentType()).thenReturn("other/type");
        when(body.getSchema()).thenReturn("schema");
        String content = "content";

        BodyValidator validator = new BodyValidator(mediaValidators());

        List<SpecificationViolation> result = validator.validate(node, body, content.getBytes());

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFailValidation() {
        Node node = new Node("body");
        String schema = "schema";
        RamlBody body = mock(RamlBody.class);
        when(body.getContentType()).thenReturn("application/json");
        when(body.getSchema()).thenReturn(schema);
        String content = "content";

        Node contentNode = new Node(node, "application/json");

        List<SpecificationViolation> expected = newArrayList(
                new SpecificationViolation(new Node(contentNode, "propertyName"), "must be provided")
        );

        when(mediaValidator.validate(contentNode, schema, content.getBytes())).thenReturn(expected);

        BodyValidator validator = new BodyValidator(mediaValidators());

        List<SpecificationViolation> result = validator.validate(node, body, content.getBytes());

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("body.application/json.propertyName", "must be provided"));
    }

    private Map<MediaType, MediaValidator> mediaValidators() {
        Map<MediaType, MediaValidator> result = newHashMap();
        result.put(MediaType.APPLICATION_JSON_TYPE, mediaValidator);
        return result;
    }
}