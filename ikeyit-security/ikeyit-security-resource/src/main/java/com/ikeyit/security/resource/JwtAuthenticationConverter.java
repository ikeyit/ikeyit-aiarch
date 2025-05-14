package com.ikeyit.security.resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashSet;
import java.util.Set;

/**
 * Convert JWT to general authentication
 */
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    public JwtAuthenticationConverter() {
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setId(Long.parseLong(jwt.getSubject()));
        authenticatedUser.setUsername(jwt.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME));
        authenticatedUser.setDisplayName(jwt.getClaimAsString(StandardClaimNames.NAME));
        authenticatedUser.setAvatar(jwt.getClaimAsString(StandardClaimNames.PICTURE));
        authenticatedUser.setLocale(jwt.getClaimAsString(StandardClaimNames.LOCALE));
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // Set the granted authorities for the client
        var clientScopes = jwt.getClaimAsStringList("scope");
        if (clientScopes != null && !clientScopes.isEmpty()) {
            for (String scope : clientScopes)
                grantedAuthorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
        }
        // Set the granted authorities for the user
        var roles = jwt.getClaimAsStringList("roles");
        if (roles != null && !roles.isEmpty()) {
            for (String role : roles)
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return new JwtUserAuthenticationToken(jwt, authenticatedUser, grantedAuthorities);
    }
}
