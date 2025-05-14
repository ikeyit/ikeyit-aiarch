package com.ikeyit.gateway.auth;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CookieCleanAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        var cookie = ResponseCookie.from("auth")
            .maxAge(0)
            .path("/")
            .build();
        exchange.getResponse().addCookie(cookie);
        return Mono.empty();
    }
}
