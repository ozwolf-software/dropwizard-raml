package net.ozwolf.raml.generator.media.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.kjetland.jackson.jsonSchema.SubclassesResolver;
import com.kjetland.jackson.jsonSchema.SubclassesResolverImpl;
import net.ozwolf.raml.generator.media.MediaFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Maps.newHashMap;
import static net.ozwolf.raml.generator.util.ScalaUtils.convertToJava;
import static net.ozwolf.raml.generator.util.ScalaUtils.convertToScala;

/**
 * <h1>JSON Schema Factory</h1>
 *
 * The provided media factory for generating JSON schemas from Jackson-annotated objects.
 *
 * To configure the ObjectMapper instance utilised by this class, use the RamlMedia instance helper values.
 */
public class JsonSchemaFactory implements MediaFactory {
    private final ObjectMapper mapper;
    private final JsonSchemaGenerator generator;

    private final static Map<Class<?>, Class<?>> REMAPPERS = newHashMap();

    public JsonSchemaFactory(ObjectMapper mapper) {
        this(null, mapper);
    }

    public JsonSchemaFactory(String baseResolverPackage,
                             ObjectMapper mapper) {
        this.mapper = mapper;
        this.generator = generator(baseResolverPackage, mapper);
    }

    @Override
    public Optional<String> create(Class<?> type, boolean collection) {
        return Optional.of(toJsonSchema(type, collection));
    }

    /**
     * Register a remapped type.  This is useful for representing one datatype as another you know it will be converted to.
     *
     * For example, if you serialize BigDecimal values as String, you can register this remapping.
     *
     * @param from the class to remap from
     * @param to the class the remap is mapped to
     */
    public static void registerRemappedType(Class<?> from, Class<?> to){
        REMAPPERS.put(from, to);
    }

    private String toJsonSchema(Class<?> type, boolean collection) {
        try {
            JsonNode schema = collection ? generator.generateJsonSchema(collectionOf(type)) : generator.generateJsonSchema(type);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error generating schema for [ " + type.getName() + " ].", e);
        }
    }

    private static JavaType collectionOf(Class<?> type) {
        return TypeFactory.defaultInstance().constructArrayType(type);
    }

    private static JsonSchemaGenerator generator(String basePackage, ObjectMapper mapper) {
        return new JsonSchemaGenerator(mapper, config(basePackage));
    }

    private static JsonSchemaConfig config(String basePackage) {
        JsonSchemaConfig config = JsonSchemaConfig.nullableJsonSchemaDraft4();
        if (StringUtils.isNotBlank(basePackage))
            config = config.withSubclassesResolver(resolver(basePackage));

        Map<Class<?>, Class<?>> remapping = convertToJava(config.classTypeReMapping());
        remapping.putAll(REMAPPERS);

        return config.copy(
                config.autoGenerateTitleForProperties(),
                config.defaultArrayFormat(),
                config.useOneOfForOption(),
                config.useOneOfForNullables(),
                config.usePropertyOrdering(),
                config.hidePolymorphismTypeProperty(),
                config.disableWarnings(),
                config.useMinLengthForNotNull(),
                config.useTypeIdForDefinitionName(),
                config.customType2FormatMapping(),
                config.useMultipleEditorSelectViaProperty(),
                config.uniqueItemClasses(),
                convertToScala(remapping),
                config.jsonSuppliers(),
                config.subclassesResolver(),
                config.failOnUnknownProperties()
        );
    }

    private static SubclassesResolver resolver(String basePackage) {
        List<String> packages = new ArrayList<>();
        packages.add(basePackage);
        return new SubclassesResolverImpl().withPackagesToScan(packages);
    }
}
