package net.ozwolf.raml.annotations.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>OAuth2 Settings</h1>
 *
 * This annotation is used to describe OAuth2 settings.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlOAuth2Settings {
    String authorizationUri();
    String accessTokenUri();
    String[] authorizationGrants();
    String[] scopes() default {};
}
