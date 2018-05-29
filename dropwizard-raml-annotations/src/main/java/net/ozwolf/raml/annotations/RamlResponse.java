package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RamlResponse {
    int status();

    String description();

    RamlParameter[] headers() default {};

    RamlBody[] bodies() default {};
}
