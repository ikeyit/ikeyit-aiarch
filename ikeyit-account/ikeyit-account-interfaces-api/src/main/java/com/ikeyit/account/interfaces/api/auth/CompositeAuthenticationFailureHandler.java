package com.ikeyit.account.interfaces.api.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompositeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final LinkedHashMap<RequestMatcher, AuthenticationFailureHandler> handlers = new LinkedHashMap<>();

    public CompositeAuthenticationFailureHandler() {
    }

    public void addAuthenticationFailureHandler(AuthenticationFailureHandler handler, RequestMatcher requestMatcher) {
        handlers.put(requestMatcher, handler);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        for (Map.Entry<RequestMatcher, AuthenticationFailureHandler> entry : handlers.entrySet()) {
            RequestMatcher key = entry.getKey();
            if (key.matches(request)) {
                entry.getValue().onAuthenticationFailure(request, response, exception);
            }
        }
    }
}
