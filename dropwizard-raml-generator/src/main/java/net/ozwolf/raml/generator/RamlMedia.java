package net.ozwolf.raml.generator;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ozwolf.raml.annotations.RamlExample;
import net.ozwolf.raml.annotations.RamlSchema;
import net.ozwolf.raml.generator.media.FromMethodExampleFactory;
import net.ozwolf.raml.generator.media.MediaFactory;
import net.ozwolf.raml.generator.media.json.JsonSchemaFactory;
import net.ozwolf.raml.generator.util.ClassPathUtils;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * <h1>Raml Media</h1>
 * <p>
 * A class for managing media handlers.
 */
public class RamlMedia {
    private Map<String, MediaTools> tools;

    private final static AtomicReference<RamlMedia> INSTANCE = new AtomicReference<>(null);

    private final static List<Consumer<RamlMedia>> APPLICATORS = newArrayList();
    private final static Map<String, MediaTools> TOOLS = newHashMap();
    private final static Map<String, ObjectMapper> DEFAULT_MAPPERS = newHashMap();
    private final static AtomicReference<Runnable> RESETTER = new AtomicReference<>(null);

    static {
        DEFAULT_MAPPERS.put("json", defaultJsonMapper());
        DEFAULT_MAPPERS.put("xml", defaultXmlMapper());
    }

    /**
     * Retrieve the instance of media mappers.
     *
     * @return the media mappers instance
     * @throws IllegalStateException if the media has not been initialized yet.
     * @see RamlMedia#initialize(String)
     */
    public static RamlMedia instance() {
        return Optional.ofNullable(INSTANCE.get()).orElseThrow(() -> new IllegalStateException("RAML media has not been initialized yet."));
    }

    /**
     * Initialize the RamlMedia class.  This then allows the static instance to be available.
     *
     * @param basePackage the base package for class scanning.  Reduced reflection footprint.
     */
    public static void initialize(String basePackage) {
        RamlMedia media = new RamlMedia(basePackage);
        APPLICATORS.forEach(c -> c.accept(media));
        media.tools.putAll(TOOLS);
        INSTANCE.set(media);
        RESETTER.set(() -> {
            destroy();
            initialize(basePackage);
        });
    }

    /**
     * Destroys the static references used by the RamlMedia instance.  This is necessary to ensure reflection class loaders can be garbage collected.
     */
    public static void destroy() {
        INSTANCE.set(null);
        System.gc();
    }

    /**
     * Reset the RAML media back to a defined state.
     *
     * @throws IllegalStateException if the media has not been initialized yet.
     * @see RamlMedia#initialize(String)
     */
    public static void reset() {
        Optional.ofNullable(RESETTER.get()).orElseThrow(() -> new IllegalStateException("RamlMedia must be initialized first.")).run();
    }

    private RamlMedia(String basePackage) {
        this.tools = defaultTools(basePackage);
    }

    /**
     * Generates a schema for the given type specific to the content type
     *
     * @param contentType the content type to generate a schema for
     * @param type        the type to generate the schema from
     * @param collection  flag indicating if the type is wrapped in a collection
     * @return the (optional) schema definition if available
     */
    public Optional<String> generateSchemaFor(String contentType, Class<?> type, boolean collection) {
        RamlSchema schemaAnnotation = type.getAnnotation(RamlSchema.class);
        if (schemaAnnotation != null)
            return Optional.ofNullable(ClassPathUtils.getResourceAsString(schemaAnnotation.value()));

        return toolsFor(contentType)
                .flatMap(t -> t.schema.create(type, collection));
    }

    /**
     * Generates an example for the given type specific to the content type
     *
     * @param contentType the content type to generate an example for
     * @param type        the type to generate the example from
     * @param collection  flag indiciating if the type is wrapped in a collection
     * @return the (optional) example if available
     */
    public Optional<String> generateExampleFor(String contentType, Class<?> type, boolean collection) {
        RamlExample exampleAnnotation = type.getAnnotation(RamlExample.class);
        if (exampleAnnotation != null)
            return Optional.ofNullable(ClassPathUtils.getResourceAsString(exampleAnnotation.value()));

        return toolsFor(contentType)
                .flatMap(t -> t.example.create(type, collection));
    }

    /**
     * Register media tools for a given content type.  Must be supportable by a Jackson mapper and must have an implementation of a schema and example media factory.
     *
     * @param contentType    the content type to register the media tools against (eg. application/xml)
     * @param mapper         the Jackson ObjectMapper instance relative to this content type
     * @param schemaFactory  the factory implementation for generating schemas from the given type
     * @param exampleFactory the factory implementation for generating examples from the given type
     */
    public static void registerToolsFor(String contentType, ObjectMapper mapper, MediaFactory schemaFactory, MediaFactory exampleFactory) {
        TOOLS.put(contentType.toLowerCase(), new MediaTools(mapper, schemaFactory, exampleFactory));
    }

    /**
     * Register a Jackson module with the mapper for the given media type.
     *
     * @param contentType the content type mapper to register with
     * @param module      the Jackson module to register
     * @throws IllegalArgumentException if no mapper available for media type
     */
    public static void registerModuleFor(String contentType, Module module) {
        APPLICATORS.add(media -> {
            media.toolsFor(contentType)
                    .map(t -> t.mapper.registerModule(module))
                    .orElseThrow(() -> new IllegalStateException("No media tools defined for [ " + contentType + " ] content type."));
        });
    }

    /**
     * Register a Jackson modules with the mapper for the given media type.
     *
     * @param contentType the content type mapper to register with
     * @param modules     the Jackson module to register
     * @throws IllegalArgumentException if no mapper available for media type
     */
    public static void registerModulesFor(String contentType, Module... modules) {
        Arrays.stream(modules).forEach(m -> registerModuleFor(contentType, m));
    }

    /**
     * Register a Jackson module via the class name string.
     * <p>
     * This option is used when driving the generator from a configuration file (such as through the Maven plugin).
     * <p>
     * The class _must_ have a default constructor.
     *
     * @param contentType the content type mapper to register with
     * @param className   the class name of the module.
     * @throws IllegalArgumentException if the class could not be found or does not have a default constructor.
     * @throws IllegalStateException    if the class could not be instantiated
     */
    @SuppressWarnings("unchecked")
    public static void registerModuleFor(String contentType, String className) {
        APPLICATORS.add(media -> {
            try {
                ObjectMapper mapper = media.toolsFor(contentType).map(t -> t.mapper).orElseThrow(() -> new IllegalArgumentException("No media tools defined for [ " + contentType + " ] content type."));

                Class<?> module = Class.forName(className);
                if (!Module.class.isAssignableFrom(module))
                    throw new IllegalArgumentException("Class [ " + className + " ] does not implement [ " + Module.class.getName() + " ]");

                Constructor<? extends Module> constructor = ((Class<? extends Module>) module).getConstructor();

                Module instance = constructor.newInstance();
                mapper.registerModule(instance);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Module [ " + className + " ] does not have a default constructor.", e);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new IllegalStateException("Could not instantiate instance of [ " + className + " ] module.", e);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Module [ " + className + " ] could not be found.", e);
            }
        });
    }

    /**
     * Register the default mapper to use for the included JSON functionality.
     * @param mapper the JSON default mapper to use
     */
    public static void registerDefaultJsonMapper(ObjectMapper mapper){
        DEFAULT_MAPPERS.put("json", mapper);
    }

    /**
     * Register the default mapper to use for the included XML functionality.
     * @param mapper the XML default mapper to use
     */
    public static void registerDefaultXmlMapper(ObjectMapper mapper){
        DEFAULT_MAPPERS.put("xml", mapper);
    }

    private Optional<MediaTools> toolsFor(String contentType) {
        return tools.keySet()
                .stream()
                .filter(k -> MediaType.valueOf(k).isCompatible(MediaType.valueOf(contentType)))
                .findFirst()
                .map(k -> tools.get(k));
    }

    private static Map<String, MediaTools> defaultTools(String basePackage) {
        Map<String, MediaTools> tools = newHashMap();

        ObjectMapper jsonMapper = DEFAULT_MAPPERS.get("json");
        tools.put(
                "application/json",
                new MediaTools(
                        jsonMapper,
                        new JsonSchemaFactory(basePackage, jsonMapper),
                        new FromMethodExampleFactory(jsonMapper)
                )
        );

        ObjectMapper xmlMapper = DEFAULT_MAPPERS.get("xml");

        tools.put(
                "text/xml",
                new MediaTools(
                        xmlMapper,
                        (t, c) -> Optional.empty(),
                        new FromMethodExampleFactory(xmlMapper)
                )
        );

        return tools;
    }

    private static ObjectMapper defaultJsonMapper() {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule()).registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    private static ObjectMapper defaultXmlMapper() {
        XmlMapper mapper = new XmlMapper(new JacksonXmlModule());
        mapper.registerModule(new JodaModule()).registerModule(new JavaTimeModule());
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    private static class MediaTools {
        private ObjectMapper mapper;
        private final MediaFactory schema;
        private final MediaFactory example;

        private MediaTools(ObjectMapper mapper,
                           MediaFactory schema,
                           MediaFactory example) {
            this.mapper = mapper;
            this.schema = schema;
            this.example = example;
        }

        private void assign(ObjectMapper mapper) {
            this.mapper = mapper;
        }
    }
}
