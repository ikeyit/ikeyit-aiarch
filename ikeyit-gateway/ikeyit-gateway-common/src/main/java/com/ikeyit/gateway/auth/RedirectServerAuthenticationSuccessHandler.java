package com.ikeyit.gateway.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public class RedirectServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private URI location = URI.create("/");

    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    public RedirectServerAuthenticationSuccessHandler() {
    }


    public RedirectServerAuthenticationSuccessHandler(String location) {
        this.location = URI.create(location);
    }


    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return getRedirectUri(exchange)
            .defaultIfEmpty(this.location)
            .flatMap((location) -> this.redirectStrategy.sendRedirect(exchange, location));
    }


    protected Mono<URI> getRedirectUri(ServerWebExchange exchange) {
        return Mono.fromSupplier(() -> {
            String redirectAfterAuth = exchange.getAttribute("redirect_after_auth");
            return redirectAfterAuth == null ? null : URI.create(redirectAfterAuth);
        });
    }

    public void setLocation(URI location) {
        Assert.notNull(location, "location cannot be null");
        this.location = location;
    }

    public void setRedirectStrategy(ServerRedirectStrategy redirectStrategy) {
        Assert.notNull(redirectStrategy, "redirectStrategy cannot be null");
        this.redirectStrategy = redirectStrategy;
    }
}
