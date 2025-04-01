package com.ikeyit.account.interfaces.api.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompositeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final LinkedHashMap<RequestMatcher, AuthenticationSuccessHandler> handlers = new LinkedHashMap<>();

    public CompositeAuthenticationSuccessHandler() {
    }

    public void addAuthenticationSuccessHandler(AuthenticationSuccessHandler handler, RequestMatcher requestMatcher) {
        handlers.put(requestMatcher, handler);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        for (Map.Entry<RequestMatcher, AuthenticationSuccessHandler> entry : handlers.entrySet()) {
            RequestMatcher key = entry.getKey();
            if (key.matches(request)) {
                entry.getValue().onAuthenticationSuccess(request, response, authentication);
            }
        }
    }
}
