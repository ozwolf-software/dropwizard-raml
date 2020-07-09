package net.ozwolf.raml.generator.media.json;

import java.util.HashMap;
import java.util.Map;

public class RamlJsonSchemaConfiguration {
    private final Map<Class<?>, Class<?>> remapping;



    private RamlJsonSchemaConfiguration(){
        this.remapping = new HashMap<>();
    }
}
