package com.ikeyit.account.interfaces.api.auth.authsession;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ikeyit.account.infrastructure.security.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthTokenAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthSession authSession;

    public AuthTokenAuthenticationToken(AuthSession authSession) {
        super(authSession.getPrincipal().getAuthorities());
        this.authSession = authSession;
        this.setAuthenticated(true);
    }

    public AuthSession getAuthSession() {
        return authSession;
    }

    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    @JsonIgnore
    public boolean isAuthenticated() {
        return super.isAuthenticated();
    }

    @Override
    @JsonIgnore
    public Object getCredentials() {
        return null;
    }

    @Override
    @JsonIgnore
    public UserPrincipal getPrincipal() {
        return authSession.getPrincipal();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return String.valueOf(authSession.getPrincipal().getId());
    }
}
