package com.ikeyit.gateway.filter;

import com.ikeyit.gateway.auth.TokenRefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class RefreshTokenFilter implements WebFilter {
    private final static Logger log = LoggerFactory.getLogger(RefreshTokenFilter.class);

    private final TokenRefreshService tokenRefreshService;

    public RefreshTokenFilter(TokenRefreshService tokenRefreshService) {
        this.tokenRefreshService = tokenRefreshService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(securityContext -> {
                if (securityContext.getAuthentication() instanceof OAuth2AuthenticationToken authentication) {
                    return tokenRefreshService
                        .checkAndRefreshToken(authentication, exchange);
                }
                return Mono.empty();
            })
            .then(chain.filter(exchange))
            .onErrorResume(OAuth2AuthorizationException.class, error -> {
                if (OAuth2ErrorCodes.INVALID_GRANT.equals(error.getError().getErrorCode())) {
                    log.warn("Refresh token is invalid!");
                    return exchange.getSession()
                        .flatMap(session -> {
                            session.getAttributes().clear();
                            return session.invalidate();
                        })
                        .then(chain.filter(exchange))
                        .contextWrite(ReactiveSecurityContextHolder.clearContext());
                }
               return Mono.error(error);
            });
    }
}
