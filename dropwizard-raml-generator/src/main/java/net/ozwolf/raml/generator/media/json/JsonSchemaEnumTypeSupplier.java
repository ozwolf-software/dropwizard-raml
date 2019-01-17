package net.ozwolf.raml.generator.media.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.function.Supplier;

public abstract class JsonSchemaEnumTypeSupplier<T extends Enum> implements Supplier<JsonNode> {
    protected JsonSchemaEnumTypeSupplier() {
    }

    @Override
    public JsonNode get() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        ArrayNode enumValues = node.putArray("enum");
        Class<T> type = getType();
        Arrays.stream(type.getEnumConstants()).forEach(e -> enumValues.add(e.name()));
        return node;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }
}
