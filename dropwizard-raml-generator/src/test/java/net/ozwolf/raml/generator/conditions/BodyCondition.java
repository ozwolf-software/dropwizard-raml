package net.ozwolf.raml.generator.conditions;

import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.Condition;
import org.raml.v2.api.model.v10.datamodel.ExampleSpec;
import org.raml.v2.api.model.v10.datamodel.ExternalTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.fail;

public class BodyCondition extends Condition<TypeDeclaration> {
    private final String contentType;

    private String schema;
    private String example;

    public BodyCondition(String contentType) {
        super("<contentType=" + contentType + ">");
        this.contentType = contentType;
    }

    public BodyCondition withSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public BodyCondition withExample(String example) {
        this.example = example;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean matches(TypeDeclaration value) {
        if (!(value instanceof ExternalTypeDeclaration))
            return false;

        ExternalTypeDeclaration e = (ExternalTypeDeclaration) value;

        if (!e.name().equals(contentType))
            return false;

        if (schema != null && !verifyContent(schema, e.schemaContent()))
            return false;

        if (example != null && !verifyContent(example, Optional.ofNullable(e.example()).map(ExampleSpec::value).orElse(null)))
            return false;

        return true;
    }

    private boolean verifyContent(String expected, String actual) {
        try {
            if (MediaType.APPLICATION_JSON_TYPE.isCompatible(MediaType.valueOf(contentType))) {
                JSONCompareResult compare = JSONCompare.compareJSON(expected, actual, JSONCompareMode.LENIENT);
                return compare.passed();
            } else {
                return StringUtils.equals(expected, actual);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not parse content comparison.", e);
        }
    }

    public static boolean matches(List<BodyCondition> expectedBodies, List<TypeDeclaration> actualBodies){
        for(BodyCondition body : expectedBodies){
            TypeDeclaration actual = actualBodies.stream().filter(b -> b.name().equals(body.getContentType())).findFirst().orElse(null);
            if (actual == null || !body.matches(actual))
                return false;
        }
        return true;
    }
}
