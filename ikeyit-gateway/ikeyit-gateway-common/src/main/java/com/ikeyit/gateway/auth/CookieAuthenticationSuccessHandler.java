package com.ikeyit.gateway.auth;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

public class CookieAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        var exchange = webFilterExchange.getExchange();
        var cookie = ResponseCookie.from("auth", "1")
            .httpOnly(false)
            .secure(false)
            .path("/")
            .maxAge(-1)
            .build();
        exchange.getResponse().addCookie(cookie);
        return Mono.empty();
    }
}

