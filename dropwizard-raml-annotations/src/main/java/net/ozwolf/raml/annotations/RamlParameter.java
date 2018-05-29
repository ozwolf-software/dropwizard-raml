package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RamlParameter {
    String name();
    String type();
    String displayName() default "";
    String description() default "";
    boolean required() default true;
    boolean multiple() default false;
    String[] allowedValues() default {};
    String example() default "";
    String pattern() default "";
    int minimum() default Integer.MIN_VALUE;
    int maximum() default Integer.MIN_VALUE;
}
