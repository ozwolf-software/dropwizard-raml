package net.ozwolf.raml.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import net.ozwolf.raml.annotations.RamlApp;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import net.ozwolf.raml.generator.model.RamlAppModel;
import org.reflections.Reflections;
import org.reflections.scanners.*;

import java.util.Set;

public class RamlGenerator {
    private final String version;
    private final Reflections reflections;

    public final static ObjectMapper MAPPER = new ObjectMapper();
    public final static ObjectMapper YAML_MAPPER = new YAMLMapper();

    private final static String HEADER = "#%RAML 1.0";

    public RamlGenerator(String basePackage, String version){
        this.version = version;
        this.reflections = new Reflections(
                basePackage,
                new MethodParameterScanner(),
                new FieldAnnotationsScanner(),
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner(),
                new SubTypesScanner()
        );
    }

    public String generate(){
        try {
            Set<Class<?>> apps = reflections.getTypesAnnotatedWith(RamlApp.class);

            if (apps.isEmpty())
                throw new IllegalStateException("No class found annotated with [ @" + RamlApp.class.getSimpleName() + " ] annotation.");

            if (apps.size() > 1)
                throw new IllegalStateException("Multiple classes found with [ @" + RamlApp.class.getSimpleName() + " ] annotation.");

            RamlApp annotation = apps.iterator().next().getAnnotation(RamlApp.class);

            RamlAppModel model = new RamlAppModel(version, annotation, reflections);

            return HEADER + "\n" + YAML_MAPPER.writeValueAsString(model);
        } catch (Exception e) {
            throw new RamlGenerationException(e);
        }
    }
}
