package net.ozwolf.raml.generator.media.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.util.Set;
import java.util.function.Supplier;

import static com.google.common.collect.Sets.newHashSet;

public abstract class JsonSchemaEnumValuesSupplier implements Supplier<JsonNode> {
    private final Set<String> values;

    protected JsonSchemaEnumValuesSupplier(Set<String> values) {
        this.values = values;
    }

    protected JsonSchemaEnumValuesSupplier(String... values) {
        this(newHashSet(values));
    }

    @Override
    public JsonNode get() {
        ArrayNode enumValues = JsonNodeFactory.instance.arrayNode();
        values.forEach(enumValues::add);
        return enumValues;
    }
}
