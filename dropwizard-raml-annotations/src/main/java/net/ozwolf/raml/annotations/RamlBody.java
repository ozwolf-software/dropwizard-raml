package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RamlBody {
    String contentType();
    Class<?> returnType() default NotDefinedReturnType.class;
    String schema() default "";
    String example() default "";

    final class NotDefinedReturnType {}
}
