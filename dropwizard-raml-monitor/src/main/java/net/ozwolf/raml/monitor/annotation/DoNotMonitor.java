package net.ozwolf.raml.monitor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>Do Not Monitor Annotation</h1>
 *
 * This annotation can be placed on resource methods to exclude the method from being monitored.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DoNotMonitor {
}
