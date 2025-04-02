package com.ikeyit.account.interfaces.api.auth.oauth2;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Customize the ID Token of OpenID Connect
 */
public class IdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final Duration idTokenTimeToLive;


    public IdTokenCustomizer(Duration idTokenTimeToLive) {
        this.idTokenTimeToLive = idTokenTimeToLive;
    }


    @Override
    public void customize(JwtEncodingContext context) {
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            customizeIDToken(context);
        }
    }

    private void customizeIDToken(JwtEncodingContext context) {
        UserPrincipal userPrincipal = (UserPrincipal) context.getPrincipal().getPrincipal();
        JwtClaimsSet.Builder claimsBuilder = context.getClaims();
        claimsBuilder.claim("username", userPrincipal.getUsername());
        claimsBuilder.claim(StandardClaimNames.SUB, userPrincipal.getId().toString());
        claimsBuilder.claim(StandardClaimNames.NAME, userPrincipal.getDisplayName());
        if (userPrincipal.getAvatar() != null)
            claimsBuilder.claim(StandardClaimNames.PICTURE, userPrincipal.getAvatar());
        if (userPrincipal.getLocale() != null)
            claimsBuilder.claim(StandardClaimNames.LOCALE, userPrincipal.getLocale());
        if (context.getAuthorizedScopes().contains("role")) {
            claimsBuilder
                .claim("scp", userPrincipal.getAuthorities().stream().map(Objects::toString)
                    .collect(Collectors.joining(",")));
        }
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(idTokenTimeToLive);
        claimsBuilder.issuedAt(issuedAt);
        claimsBuilder.expiresAt(expiresAt);
    }
}
