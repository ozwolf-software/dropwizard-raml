package net.ozwolf.raml.generator;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.annotations.RamlApp;
import net.ozwolf.raml.annotations.RamlIgnore;
import net.ozwolf.raml.generator.exception.RamlGenerationError;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import net.ozwolf.raml.generator.exception.RamlGenerationUnhandledException;
import net.ozwolf.raml.generator.factory.ResourceFactory;
import net.ozwolf.raml.generator.model.RamlAppModel;
import org.reflections.Reflections;
import org.reflections.scanners.*;

import javax.ws.rs.Path;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;

/**
 * <h1>RAML Generator</h1>
 *
 * This class is designed to read across the supplied base package and generate a valid RAML specification from the use of the RAML annotations and JAX-RS + Jackson entities within.
 *
 * The generator will resolve resources into inline content as part of this process to create a singular RAML specification that is not reliant on classpathing.
 */
public class RamlGenerator {
    private final String version;
    private final Reflections reflections;
    private ResourceFactory resourceFactory;

    public final static ObjectMapper MAPPER = new ObjectMapper().registerModule(new JodaModule()).registerModule(new JavaTimeModule());
    private final static ObjectMapper YAML_MAPPER = new YAMLMapper();

    private final static String HEADER = "#%RAML 1.0";

    /**
     * Construct a new RAML generator.
     *
     * The generator requires a base package to scan from so as to limit reflection computations.
     *
     * @param basePackage the base package of the application generate documetnation for
     * @param version     the application version
     */
    public RamlGenerator(String basePackage, String version) {
        this.version = version;
        this.reflections = new Reflections(
                basePackage,
                new MethodParameterScanner(),
                new FieldAnnotationsScanner(),
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner(),
                new SubTypesScanner()
        );
        this.resourceFactory = new ResourceFactory();
    }

    /**
     * Register a Jackson module with the generator.
     *
     * This is important if your application uses any custom tooling with Jackson (such as custom serialization, etc).
     *
     * @param module the Jackson module
     */
    public void registerModule(Module module) {
        MAPPER.registerModule(module);
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
    public void registerModule(String className) {
        try {
            Class<?> module = Class.forName(className);
            if (!Module.class.isAssignableFrom(module))
                throw new IllegalArgumentException("Class [ " + className + " ] does not implement [ " + Module.class.getName() + " ]");

            Constructor<? extends Module> constructor = ((Class<? extends Module>) module).getConstructor();

            Module instance = constructor.newInstance();
            MAPPER.registerModule(instance);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Module [ " + className + " ] does not have a default constructor.", e);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException("Could not instantiate instance of [ " + className + " ] module.", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Module [ " + className + " ] could not be found.", e);
        }
    }

    /**
     * Generate the RAML documentation for the service.
     *
     * @return the RAML as a string
     * @throws RamlGenerationException          if the generation process encountered any known errors
     * @throws RamlGenerationUnhandledException if an unexpected error occurred during the generation process
     */
    public String generate() throws RamlGenerationException {
        try {
            Set<Class<?>> apps = reflections.getTypesAnnotatedWith(RamlApp.class);

            if (apps.isEmpty())
                throw new IllegalStateException("No class found annotated with [ @" + RamlApp.class.getSimpleName() + " ] annotation.");

            if (apps.size() > 1)
                throw new IllegalStateException("Multiple classes found with [ @" + RamlApp.class.getSimpleName() + " ] annotation.");

            RamlApp annotation = apps.iterator().next().getAnnotation(RamlApp.class);

            List<RamlGenerationError> errors = newArrayList();

            Consumer<RamlGenerationError> onError = errors::add;

            RamlAppModel model = new RamlAppModel(version, annotation);

            reflections.getTypesAnnotatedWith(Path.class)
                    .stream()
                    .filter(r -> !r.isAnnotationPresent(RamlIgnore.class))
                    .forEach(r -> resourceFactory.getResource(r, model::addResource, onError));

            if (!errors.isEmpty())
                throw new RamlGenerationException(errors);
            return HEADER + "\n" + YAML_MAPPER.writeValueAsString(model);
        } catch (RamlGenerationException e) {
            throw e;
        } catch (Exception e) {
            throw new RamlGenerationUnhandledException(e);
        }
    }

    @VisibleForTesting
    protected void setResourceFactory(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }
}
