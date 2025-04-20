package com.ikeyit.security.resource;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

public class CookieBearerTokenResolver implements BearerTokenResolver {
    public static final String DEFAULT_JWT_COOKIE_NAME = "st";
    private String cookieName;

    public CookieBearerTokenResolver() {
        this(DEFAULT_JWT_COOKIE_NAME);
    }

    public CookieBearerTokenResolver(String cookieName) {
        this.cookieName = cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
