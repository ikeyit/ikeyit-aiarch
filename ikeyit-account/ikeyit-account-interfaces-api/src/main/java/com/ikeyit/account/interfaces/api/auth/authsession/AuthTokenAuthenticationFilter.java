package com.ikeyit.account.interfaces.api.auth.authsession;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;

import java.io.IOException;

public class AuthTokenAuthenticationFilter extends BearerTokenAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthTokenAuthenticationFilter.class);

    private final AuthTokenCookieRepository authTokenCookieRepository;

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();


    public AuthTokenAuthenticationFilter(AuthenticationManager authenticationManager,
                                         AuthTokenCookieRepository authTokenCookieRepository) {
        super(authenticationManager);
        this.authTokenCookieRepository = authTokenCookieRepository;
        this.setBearerTokenResolver((authTokenCookieRepository::getToken));
        this.setAuthenticationFailureHandler((request, response, e) -> {
            throw e;
        });
    }

    public SecurityContextHolderStrategy getSecurityContextHolderStrategy() {
        return securityContextHolderStrategy;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            super.doFilterInternal(request, response, filterChain);
        } catch (AuthSessionMissedException | OAuth2AuthenticationException e) {
            log.error(e.getMessage());
            authTokenCookieRepository.clearCookie(request, response);
            // ignore it, continue the filter chain.
            this.securityContextHolderStrategy.createEmptyContext();
            filterChain.doFilter(request, response);
        }
    }
}
