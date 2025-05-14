package com.ikeyit.gateway.auth;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

public class CookieCleanLogoutSuccessHandler implements ServerLogoutSuccessHandler {
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        var cookie = ResponseCookie.from("auth")
            .path("/")
            .maxAge(0)
            .build();
        exchange.getExchange().getResponse().addCookie(cookie);
        return Mono.empty();
    }
}
