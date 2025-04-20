package com.ikeyit.account.interfaces.api.auth.authsession;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Auth token is a jwt. After the jwt is verified, we need read the whole auth session data from the storage. Then convert it
 * to AuthTokenAuthenticationToken!
 */
public class AuthTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private String claimName = "asid";

    private final AuthSessionService authSessionService;

    public AuthTokenAuthenticationConverter(AuthSessionService authSessionService) {
        this.authSessionService = authSessionService;
    }

    public AuthTokenAuthenticationConverter(AuthSessionService authSessionService, String claimName) {
        this.authSessionService = authSessionService;
        this.claimName = claimName;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // the auth session is stored in the jwt claim.
        String id = jwt.getClaim(claimName);
        AuthSession authSession = authSessionService.getAuthSession(id);
        // Though the jwt is valid, it still may be missed. Because it can be revoked.
        if (authSession == null) {
            throw new AuthSessionMissedException("Auth session is missed! " + id);
        }
        return new AuthTokenAuthenticationToken(authSession);
    }
}
