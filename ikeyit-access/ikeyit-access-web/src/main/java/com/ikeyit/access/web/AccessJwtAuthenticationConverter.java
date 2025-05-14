package com.ikeyit.access.web;

import com.ikeyit.access.core.AccessService;
import com.ikeyit.access.core.AuthoritiesRequest;
import com.ikeyit.security.resource.AuthenticatedUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;

public class AccessJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final AccessService accessService;

    public AccessJwtAuthenticationConverter(AccessService accessService) {
        this.accessService = accessService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setId(Long.parseLong(jwt.getSubject()));
        authenticatedUser.setUsername(jwt.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME));
        authenticatedUser.setDisplayName(jwt.getClaimAsString(StandardClaimNames.NAME));
        authenticatedUser.setAvatar(jwt.getClaimAsString(StandardClaimNames.PICTURE));
        authenticatedUser.setLocale(jwt.getClaimAsString(StandardClaimNames.LOCALE));
        var accessContext = AccessContextHolder.getContextOrThrow();
        var grantedAuthorities = accessService.getAuthorities(new AuthoritiesRequest(authenticatedUser.getId(), accessContext.getRealmId(), accessContext.getRealmType()));
        var grantedAuthoritySet = new HashSet<GrantedAuthority>();
        for (var role : grantedAuthorities.getRoles()) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        if (!CollectionUtils.isEmpty(grantedAuthorities.getRoles())) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority("MEMBER"));
        }
        for (var permission : grantedAuthorities.getPermissions()) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority(permission));
        }
        authenticatedUser.setAttribute("grantedAuthorities", grantedAuthorities);
        return new UserAuthenticationToken(jwt, authenticatedUser, grantedAuthoritySet);
    }
}
