package net.ozwolf.raml.generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.ozwolf.raml.annotations.RamlSecurity;
import net.ozwolf.raml.annotations.security.RamlOAuth1Settings;
import net.ozwolf.raml.annotations.security.RamlOAuth2Settings;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static net.ozwolf.raml.generator.util.CollectionUtils.nullIfEmpty;
import static org.apache.commons.lang.StringUtils.trimToNull;

@JsonSerialize
@JsonPropertyOrder({"type", "description", "describedBy", "settings"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RamlSecurityModel {
    private final String type;
    private final String description;
    private final RamlDescribedByModel describedBy;
    private final Object settings;

    public RamlSecurityModel(RamlSecurity annotation) {
        this.type = annotation.type();
        this.description = annotation.description();
        this.describedBy = new RamlDescribedByModel(annotation.describedBy());
        this.settings = createSettings(annotation);
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonPropertyOrder("description")
    public String getDescription() {
        return trimToNull(description);
    }

    @JsonProperty("describedBy")
    public RamlDescribedByModel getDescribedBy() {
        return describedBy;
    }

    @JsonProperty("settings")
    public Object getSettings() {
        return settings;
    }

    private static Object createSettings(RamlSecurity annotation){
        if (annotation.type().equalsIgnoreCase("OAuth 1.0")){
            return new OAuth1SettingsModel(annotation.oauth1Settings());
        } else if (annotation.type().equalsIgnoreCase("OAuth 2.0")){
            return new OAuth2SettingsModel(annotation.oauth2Settings());
        } else {
            return null;
        }
    }

    @JsonSerialize
    @JsonPropertyOrder({"requestTokenUri", "authorizationUri", "tokenCredentialsUri", "signatures"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OAuth1SettingsModel {
        private final String requestTokenUri;
        private final String authorizationUri;
        private final String tokenCredentialsUri;
        private final List<String> signatures;

        private OAuth1SettingsModel(RamlOAuth1Settings settings){
            this.requestTokenUri = settings.requestTokenUri();
            this.authorizationUri = settings.authorizationUri();
            this.tokenCredentialsUri = settings.tokenCredentialsUri();
            this.signatures = Arrays.asList(settings.signatures());
        }

        @JsonProperty("requestTokenUri")
        public String getRequestTokenUri() {
            return trimToNull(requestTokenUri);
        }

        @JsonProperty("authorizationUri")
        public String getAuthorizationUri() {
            return trimToNull(authorizationUri);
        }

        @JsonProperty("tokenCredentialsUri")
        public String getTokenCredentialsUri() {
            return trimToNull(tokenCredentialsUri);
        }

        @JsonProperty("signatures")
        public List<String> getSignatures() {
            return nullIfEmpty(signatures);
        }
    }

    @JsonSerialize
    @JsonPropertyOrder({"authorizationUri", "accessTokenUri", "authorizationGrants", "scopes"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OAuth2SettingsModel {
        private final String authorizationUri;
        private final String accessTokenUri;
        private final List<String> authorizationGrants;
        private final List<String> scopes;

        private OAuth2SettingsModel(RamlOAuth2Settings settings){
            this.authorizationUri = settings.authorizationUri();
            this.accessTokenUri = settings.accessTokenUri();
            this.authorizationGrants = Arrays.asList(settings.authorizationGrants());
            this.scopes = Arrays.asList(settings.scopes());
        }

        @JsonProperty("authorizationUri")
        public String getAuthorizationUri() {
            return authorizationUri;
        }

        @JsonProperty("accessTokenUri")
        public String getAccessTokenUri() {
            return accessTokenUri;
        }

        @JsonProperty("authorizationGrants")
        public List<String> getAuthorizationGrants() {
            return authorizationGrants;
        }

        @JsonProperty("scopes")
        public List<String> getScopes() {
            return scopes;
        }
    }
}
