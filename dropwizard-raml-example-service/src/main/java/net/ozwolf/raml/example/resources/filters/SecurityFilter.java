package net.ozwolf.raml.example.resources.filters;

import net.ozwolf.raml.example.security.Secured;
import net.ozwolf.raml.example.security.Users;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityFilter implements ContainerRequestFilter {
    private final Secured secured;

    private final static Pattern HEADER_PATTERN = Pattern.compile("(?<type>.+?) (?<token>.+)");

    public SecurityFilter(Secured secured) {
        this.secured = secured;
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        String header = request.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (header == null)
            throw new NotAuthorizedException("unknown");

        Matcher matcher = HEADER_PATTERN.matcher(header);
        if (!matcher.matches())
            throw new NotAuthorizedException("unknown");

        String type = matcher.group("type");

        if (type.equalsIgnoreCase("Bearer")){
            if (!secured.bearers() || Users.isOAuth2(matcher.group("token")))
                throw new ForbiddenException();
        } else if (type.equalsIgnoreCase("User")) {
            if (!secured.users() || Users.isUser(matcher.group("token")))
                throw new ForbiddenException();
        } else {
            throw new NotAuthorizedException(type);
        }
    }

    public static class Feature implements DynamicFeature {
        @Override
        public void configure(ResourceInfo r, FeatureContext c) {
            Secured secured = r.getResourceMethod().getAnnotation(Secured.class);
            if (secured != null)
                c.register(new SecurityFilter(secured));
        }
    }
}
