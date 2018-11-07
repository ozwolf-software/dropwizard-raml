package net.ozwolf.raml.apidocs.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PACKAGE})
@JacksonAnnotation
@JacksonFeatures
public @interface MarkdownToHTML {
}
