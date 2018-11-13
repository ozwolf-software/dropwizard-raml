package net.ozwolf.raml.annotations.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>OAuth1 Settings</h1>
 *
 * This annotation is used to describe OAuth1 settings.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlOAuth1Settings {
    String requestTokenUri();
    String authorizationUri();
    String tokenCredentialsUri();
    String[] signatures() default {};
}
