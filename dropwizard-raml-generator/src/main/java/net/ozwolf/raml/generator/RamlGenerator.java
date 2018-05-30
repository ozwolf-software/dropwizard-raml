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

public class RamlGenerator {
    private final String version;
    private final Reflections reflections;
    private ResourceFactory resourceFactory;

    public final static ObjectMapper MAPPER = new ObjectMapper().registerModule(new JodaModule()).registerModule(new JavaTimeModule());
    private final static ObjectMapper YAML_MAPPER = new YAMLMapper();

    private final static String HEADER = "#%RAML 1.0";

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

    public void registerModule(Module module) {
        MAPPER.registerModule(module);
    }

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

    public String generate() throws RamlGenerationException {
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
                .forEach(r -> resourceFactory.apply(r, model::addResource, onError));

        if (!errors.isEmpty())
            throw new RamlGenerationException(errors);

        try {
            return HEADER + "\n" + YAML_MAPPER.writeValueAsString(model);
        } catch (Exception e) {
            throw new RamlGenerationUnhandledException(e);
        }
    }

    @VisibleForTesting
    protected void setResourceFactory(ResourceFactory resourceFactory){
        this.resourceFactory = resourceFactory;
    }
}
