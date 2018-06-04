package net.ozwolf.raml.generator;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ozwolf.raml.generator.media.FromMethodExampleFactory;
import net.ozwolf.raml.generator.media.MediaFactory;
import net.ozwolf.raml.generator.media.json.JsonSchemaFactory;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Maps.newHashMap;

/**
 * <h1>Raml Media</h1>
 *
 * A class for managing media handlers.
 */
public class RamlMedia {
    private Map<String, MediaTools> tools;

    private final static RamlMedia INSTANCE = new RamlMedia();

    /**
     * Retrieve the instance of media mappers.
     *
     * @return the media mappers instance
     */
    public static RamlMedia instance() {
        return INSTANCE;
    }

    private RamlMedia() {
        this.tools = defaultTools();
    }

    /**
     * Generates a schema for the given type specific to the content type
     *
     * @param contentType the content type to generate a schema for
     * @param type        the type to generate the schema from
     * @return the (optional) schema definition if available
     */
    public Optional<String> generateSchemaFor(String contentType, Class<?> type) {
        return toolsFor(contentType)
                .flatMap(t -> t.schema.create(type, t.mapper));
    }

    /**
     * Generates an example for the given type specific to the content type
     *
     * @param contentType the content type to generate an example for
     * @param type        the type to generate the example from
     * @return the (optional) example if available
     */
    public Optional<String> generateExampleFor(String contentType, Class<?> type) {
        return toolsFor(contentType)
                .flatMap(t -> t.example.create(type, t.mapper));
    }

    /**
     * Register media tools for a given content type.  Must be supportable by a Jackson mapper and must have an implementation of a schema and example media factory.
     *
     * @param contentType    the content type to register the media tools against (eg. application/xml)
     * @param mapper         the Jackson ObjectMapper instance relative to this content type
     * @param schemaFactory  the factory implementation for generating schemas from the given type
     * @param exampleFactory the factory implementation for generating examples from the given type
     */
    public void registerToolsFor(String contentType, ObjectMapper mapper, MediaFactory schemaFactory, MediaFactory exampleFactory) {
        this.tools.put(contentType.toLowerCase(), new MediaTools(mapper, schemaFactory, exampleFactory));
    }

    /**
     * Register a Jackson module with the mapper for the given media type.
     *
     * @param contentType the content type mapper to register with
     * @param module      the Jackson module to register
     * @throws IllegalArgumentException if no mapper available for media type
     */
    public void registerModuleFor(String contentType, Module module) {
        toolsFor(contentType)
                .map(t -> t.mapper.registerModule(module))
                .orElseThrow(() -> new IllegalStateException("No media tools defined for [ " + contentType + " ] content type."));
    }

    /**
     * Register a Jackson module via the class name string.
     *
     * This option is used when driving the generator from a configuration file (such as through the Maven plugin).
     *
     * The class _must_ have a default constructor.
     *
     * @param contentType the content type mapper to register with
     * @param className   the class name of the module.
     * @throws IllegalArgumentException if the class could not be found or does not have a default constructor.
     * @throws IllegalStateException    if the class could not be instantiated
     */
    @SuppressWarnings("unchecked")
    public void registerModuleFor(String contentType, String className) {
        try {
            ObjectMapper mapper = toolsFor(contentType).map(t -> t.mapper).orElseThrow(() -> new IllegalArgumentException("No media tools defined for [ " + contentType + " ] content type."));

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
    }

    /**
     * Resets the media configuration back to the default state, removing all modules and custom toolsets.
     */
    public void reset() {
        this.tools = defaultTools();
    }

    private Optional<MediaTools> toolsFor(String contentType) {
        return tools.keySet()
                .stream()
                .filter(k -> MediaType.valueOf(k).isCompatible(MediaType.valueOf(contentType)))
                .findFirst()
                .map(k -> tools.get(k));
    }

    private static Map<String, MediaTools> defaultTools() {
        Map<String, MediaTools> tools = newHashMap();

        tools.put(
                "application/json",
                new MediaTools(
                        defaultJsonMapper(),
                        new JsonSchemaFactory(),
                        new FromMethodExampleFactory()
                )
        );

        tools.put(
                "text/xml",
                new MediaTools(
                        defaultXmlMapper(),
                        (t, m) -> Optional.empty(),
                        new FromMethodExampleFactory()
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
        private MediaFactory schema;
        private MediaFactory example;

        private MediaTools(ObjectMapper mapper, MediaFactory schema, MediaFactory example) {
            this.mapper = mapper;
            this.schema = schema;
            this.example = example;
        }
    }
}
