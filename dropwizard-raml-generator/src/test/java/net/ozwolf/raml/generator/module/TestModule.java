package net.ozwolf.raml.generator.module;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestModule extends SimpleModule {
    public TestModule() {
        this.addSerializer(BigDecimal.class, new BigDecimalSerializer());
    }

    public static class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(BigDecimal value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            generator.writeString(value.setScale(4, RoundingMode.HALF_UP).toPlainString());
        }
    }
}
