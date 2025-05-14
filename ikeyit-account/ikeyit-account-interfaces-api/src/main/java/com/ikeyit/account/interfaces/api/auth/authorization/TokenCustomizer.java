package com.ikeyit.account.interfaces.api.auth.authorization;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Customize access token, id token, refresh token
 */
public class TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    public TokenCustomizer() {
    }

    @Override
    public void customize(JwtEncodingContext context) {
        var userPrincipal = (UserPrincipal) context.getPrincipal().getPrincipal();
        var tokenType = context.getTokenType().getValue();
        var scopes = context.getAuthorizedScopes();
        JwtClaimsSet.Builder claimsBuilder = context.getClaims();
        claimsBuilder.subject(userPrincipal.getId().toString());
        if (OidcParameterNames.ID_TOKEN.equals(tokenType)) {
            if (scopes.contains("email")) {
                claimsBuilder.claim(StandardClaimNames.EMAIL,
                    Objects.requireNonNullElse(userPrincipal.getEmail(), ""));
            }
            if (scopes.contains("phone")) {
                claimsBuilder.claim(StandardClaimNames.PHONE_NUMBER,
                    Objects.requireNonNullElse(userPrincipal.getPhone(), ""));
            }
            addCommonClaims(userPrincipal, scopes, claimsBuilder);
            Optional.ofNullable(context.getAuthorization())
                .map(authorization -> (OAuth2AuthorizationRequest) authorization.getAttribute(OAuth2AuthorizationRequest.class.getName()))
                .map(OAuth2AuthorizationRequest::getAdditionalParameters)
                .map(params -> (String) params.get("asid"))
                .ifPresent(sid -> claimsBuilder.claim("asid", sid));
        } else if (OAuth2TokenType.ACCESS_TOKEN.getValue().equals(tokenType)) {
            addCommonClaims(userPrincipal, scopes, claimsBuilder);
        }
    }


    private void addCommonClaims(UserPrincipal userPrincipal, Set<String> scopes, JwtClaimsSet.Builder claimsBuilder) {
        if (scopes.contains("profile")) {
            // identified username to login
            claimsBuilder.claim(StandardClaimNames.PREFERRED_USERNAME, userPrincipal.getUsername());
            // avatar
            claimsBuilder.claim(StandardClaimNames.PICTURE, userPrincipal.getAvatar());
            claimsBuilder.claim(StandardClaimNames.LOCALE, userPrincipal.getLocale());
            // full name
            claimsBuilder.claim(StandardClaimNames.NAME, userPrincipal.getDisplayName());
        }
        if (scopes.contains("roles")) {
            claimsBuilder.claim("roles", userPrincipal.getRoles());
        }
    }
}
