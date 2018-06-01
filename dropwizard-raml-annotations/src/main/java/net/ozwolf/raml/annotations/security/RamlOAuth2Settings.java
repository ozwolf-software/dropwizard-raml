package net.ozwolf.raml.annotations.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RamlOAuth2Settings {
    String authorizationUri();
    String accessTokenUri();
    String[] authorizationGrants();
    String[] scopes() default {};
}
