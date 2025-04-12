package com.ikeyit.account.interfaces.api.auth.authorization;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                List<String> roles = userPrincipal.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
                claimsBuilder.claim("roles", roles);
            }
        } else if (OAuth2TokenType.ACCESS_TOKEN.getValue().equals(tokenType)) {
            if (scopes.contains("roles")) {
                List<String> roles = userPrincipal.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
                claimsBuilder.claim("roles", roles);
            }
        }
    }
}
