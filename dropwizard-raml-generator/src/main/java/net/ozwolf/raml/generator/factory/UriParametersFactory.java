package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.generator.model.RamlParameterModel;
import org.glassfish.jersey.uri.UriTemplate;

import javax.ws.rs.Path;
import java.util.Map;

public class UriParametersFactory {
    public static FactoryResult<Map<String, RamlParameterModel>> create(Class<?> resource){
        Path path = resource.getAnnotation(Path.class);

        if (path == null)
            return null;

        UriTemplate template = new UriTemplate(path.value());
        if (template.getNumberOfTemplateVariables() == 0)
            return null;
    }
}
