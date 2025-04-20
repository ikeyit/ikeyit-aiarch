package com.ikeyit.account.interfaces.api.auth.authsession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.NoOpAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.util.Assert;

public class AuthSessionConfigurer<B extends HttpSecurityBuilder<B>>
    extends AbstractHttpConfigurer<AuthSessionConfigurer<B>, B> {

    private AuthTokenCookieRepository authTokenCookieRepository;

    private AuthenticationManager authenticationManager;

    public AuthSessionConfigurer<B> authTokenCookieRepository(AuthTokenCookieRepository authTokenCookieRepository) {
        Assert.notNull(authTokenCookieRepository, "authTokenCookieRepository cannot be null");
        this.authTokenCookieRepository = authTokenCookieRepository;
        return this;
    }

    public AuthSessionConfigurer<B> authenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    @Override
    public void init(B http) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
    }

    @Override
    public void configure(B http) {
        AuthTokenAuthenticationFilter filter = new AuthTokenAuthenticationFilter(
            authenticationManager,
            authTokenCookieRepository);
        filter.setAuthenticationEntryPoint(new NoOpAuthenticationEntryPoint()); // never is used
        filter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
        http.addFilterBefore(filter, LogoutFilter.class);
    }
}
