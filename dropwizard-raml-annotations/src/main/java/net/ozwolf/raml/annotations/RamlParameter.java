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
    Class<? extends Enum> allowedValuesEnum() default NoEnum.class;
    String example() default "";
    String pattern() default "";
    String defaultValue() default "";
    long minimum() default Long.MIN_VALUE;
    long maximum() default Long.MIN_VALUE;

    enum NoEnum{}
}
