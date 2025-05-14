package com.ikeyit.gateway.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

public class RedirectServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    private final String location;

    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    public RedirectServerAuthenticationFailureHandler(String location) {
        Assert.notNull(location, "location cannot be null");
        this.location = location;
    }


    public void setRedirectStrategy(ServerRedirectStrategy redirectStrategy) {
        Assert.notNull(redirectStrategy, "redirectStrategy cannot be null");
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        URI uri = UriComponentsBuilder.fromUriString(this.location)
            .queryParam("error", exception.getMessage())
            .build()
            .toUri();
        return this.redirectStrategy.sendRedirect(webFilterExchange.getExchange(), uri);
    }

}
