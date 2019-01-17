package net.ozwolf.raml.generator.media.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.assertj.core.api.Condition;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class JsonSchemaEnumValuesSupplierTest {
    @Test
    public void shouldGenerateEnumNode(){
        JsonNode node = new JsonSchemaEnumValuesSupplier("value1", "value2"){}.get();

        assertThat(node).isInstanceOf(ArrayNode.class);

        ArrayNode a = (ArrayNode) node;
        assertThat(a)
                .hasSize(2)
                .areAtLeastOne(valueOf("value1"))
                .areAtLeastOne(valueOf("value2"));

    }

    private static Condition<JsonNode> valueOf(String nodeValue){
        return new Condition<JsonNode>(){
            @Override
            public boolean matches(JsonNode value) {
                return value.asText().equals(nodeValue);
            }
        };
    }
}