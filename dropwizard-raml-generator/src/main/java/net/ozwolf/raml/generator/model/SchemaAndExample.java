package net.ozwolf.raml.generator.model;

import java.util.Optional;

public class SchemaAndExample {
    private final String schema;
    private final String example;

    public final static SchemaAndExample NONE = new SchemaAndExample(null, null);

    public SchemaAndExample(String schema, String example) {
        this.schema = schema;
        this.example = example;
    }

    public Optional<String> getSchema(){
        return Optional.ofNullable(schema);
    }

    public Optional<String> getExample(){
        return Optional.ofNullable(example);
    }
}
