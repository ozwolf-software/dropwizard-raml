package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RamlSecurity {
    String key();
    String type();
    String description() default "";
    RamlDescriptor describedBy();
}
