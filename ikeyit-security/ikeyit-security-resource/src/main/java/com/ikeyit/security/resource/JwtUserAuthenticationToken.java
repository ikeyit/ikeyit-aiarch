package com.ikeyit.security.resource;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class JwtUserAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthenticatedUser principal;

    private final Jwt jwt;

    public JwtUserAuthenticationToken(Jwt jwt, AuthenticatedUser principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
        this.principal = principal;
        setAuthenticated(true);
    }

    public Jwt getJwt() {
        return jwt;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public AuthenticatedUser getPrincipal() {
        return principal;
    }

    @Override
    public String getName() {
        return principal.getId().toString();
    }
}
