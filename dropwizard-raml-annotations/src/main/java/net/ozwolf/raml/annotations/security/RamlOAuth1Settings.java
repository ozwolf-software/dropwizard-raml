package net.ozwolf.raml.annotations.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RamlOAuth1Settings {
    String requestTokenUri();
    String authorizationUri();
    String tokenCredentialsUri();
    String[] signatures() default {};
}
