package com.ikeyit.account.interfaces.api.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompositeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();

    public void addAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint, RequestMatcher requestMatcher) {
        entryPoints.put(requestMatcher, authenticationEntryPoint);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        for (Map.Entry<RequestMatcher, AuthenticationEntryPoint> entry : entryPoints.entrySet()) {
            RequestMatcher key = entry.getKey();
            if (key.matches(request)) {
                entry.getValue().commence(request, response, authException);
            }
        }
    }
}
