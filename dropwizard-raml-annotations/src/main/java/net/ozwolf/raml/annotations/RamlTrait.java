package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RamlTrait {
    String key();
    RamlDescriptor describedBy();
}
