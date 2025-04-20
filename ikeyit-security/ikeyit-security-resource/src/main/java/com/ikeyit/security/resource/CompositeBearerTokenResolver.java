package com.ikeyit.security.resource;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.ArrayList;
import java.util.List;

public class CompositeBearerTokenResolver implements BearerTokenResolver {
    private final List<BearerTokenResolver> resolvers = new ArrayList<>();

    public void addBearerTokenResolver(BearerTokenResolver resolver) {
        resolvers.add(resolver);
    }

    @Override
    public String resolve(HttpServletRequest request) {
        for (BearerTokenResolver resolver : resolvers) {
            String token = resolver.resolve(request);
            if (token != null) {
                return token;
            }
        }
        return null;
    }
}
