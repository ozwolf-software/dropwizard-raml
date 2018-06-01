package net.ozwolf.raml.generator;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ozwolf.raml.generator.media.SupportedMediaType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Maps.newHashMap;

/**
 * <h1>Media Mappers</h1>
 *
 * A class to contain the collection of supported media mappers used by the RAML generator to create schemas and examples.
 */
public class MediaMappers {
    private Map<SupportedMediaType, ObjectMapper> mappers;

    private final static MediaMappers INSTANCE = new MediaMappers();

    /**
     * Retrieve the instance of media mappers.
     * @return the media mappers instance
     */
    public static MediaMappers instance() {
        return INSTANCE;
    }

    private MediaMappers() {
        this.mappers = defaultMappers();
    }

    /**
     * Retrieve the mapper for the supplied supported media.
     *
     * @param mediaType the media type to get the mapper for
     *
     * @return the Jackson ObjectMapper for the media type.
     * @throws IllegalArgumentException if no mapper available for media type
     */
    public ObjectMapper mapperFor(SupportedMediaType mediaType){
        return Optional.ofNullable(mappers.get(mediaType)).orElseThrow(() -> new IllegalArgumentException("No mapper defined for [ " + mediaType + " ]."));
    }

    /**
     * Replace a provisioned mapper with a Jackson ObjectMapper of your own choosing.
     * @param mediaType the media type to replace with
     * @param mapper the mapper
     */
    public void replaceMapperFor(SupportedMediaType mediaType, ObjectMapper mapper){
        this.mappers.put(mediaType, mapper);
    }

    /**
     * Register a Jackson module with the mapper for the given media type.
     *
     * @param mediaType the media type mapper to register with
     * @param module the Jackson module to register
     *
     * @throws IllegalArgumentException if no mapper available for media type
     */
    public void registerModuleFor(SupportedMediaType mediaType, Module module){
       mapperFor(mediaType).registerModule(module);
    }

    /**
     * Register a Jackson module via the class name string.
     *
     * This option is used when driving the generator from a configuration file (such as through the Maven plugin).
     *
     * The class _must_ have a default constructor.
     *
     * @param className the class name of the module.
     * @throws IllegalArgumentException if the class could not be found or does not have a default constructor.
     * @throws IllegalStateException    if the class could not be instantiated
     */
    @SuppressWarnings("unchecked")
    public void registerModuleFor(String mediaType, String className){
        try {
            SupportedMediaType media = SupportedMediaType.find(mediaType);
            ObjectMapper mapper = mapperFor(media);

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

    public void reset(){
        this.mappers = defaultMappers();
    }

    private static Map<SupportedMediaType, ObjectMapper> defaultMappers() {
        Map<SupportedMediaType, ObjectMapper> mappers = newHashMap();

        mappers.put(SupportedMediaType.JSON, defaultJsonMapper());

        return mappers;
    }

    private static ObjectMapper defaultJsonMapper(){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule()).registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
