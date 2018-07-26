package net.ozwolf.raml.test.security;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Users {
    private final static List<String> USERS = newArrayList();
    private final static List<String> BEARERS = newArrayList();

    static {
        USERS.add("token1");
        USERS.add("token2");

        BEARERS.add("oauth2_token1");
        BEARERS.add("oauth2_token2");
    }

    public static boolean isUser(String token) {
        return USERS.stream().anyMatch(v -> v.equals(token));
    }

    public static boolean isOAuth2(String token) {
        return BEARERS.stream().anyMatch(v -> v.equals(token));
    }
}
