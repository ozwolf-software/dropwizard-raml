package net.ozwolf.raml.validator.media;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class JsonMediaValidator implements MediaValidator {
    private JsonMediaValidator() {
    }

    @Override
    public List<SpecificationViolation> validate(Node node, String schema, byte[] content) {
        List<SpecificationViolation> violations = newArrayList();

        if (StringUtils.trimToNull(schema) == null)
            return violations;

        try {
            JsonNode schemaNode = JsonLoader.fromString(schema);
            JsonNode payloadNode = JsonLoader.fromString(new String(content));

            ProcessingReport report = JsonSchemaFactory.byDefault().getJsonSchema(schemaNode).validate(payloadNode);

            if (!report.isSuccess()) {
                for (ProcessingMessage message : report) {
                    Node propertyNode = getPathFrom(node, message);
                    violations.add(new SpecificationViolation(propertyNode, message.getMessage()));
                }
            }
        } catch (Exception e) {
            violations.add(new SpecificationViolation(node.createChild("schema"), "Unxpected error validating JSON content: " + e.getMessage()));
        }

        return violations;
    }

    private static Node getPathFrom(Node parent, ProcessingMessage message) {
        String propertyName = message.asJson().get("schema").get("pointer").asText().replaceAll("/properties/", "");
        String[] pathParts = StringUtils.split(propertyName, "/");

        Node result = parent;
        for (String part : pathParts)
            result = new Node(result, part);

        return result;
    }

    public static JsonMediaValidator newInstance() {
        return new JsonMediaValidator();
    }
}
