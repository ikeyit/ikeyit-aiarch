package com.ikeyit.account.interfaces.api.auth.authorization;

import com.ikeyit.account.interfaces.api.auth.authsession.AuthTokenAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.HashMap;

public class AuthorizationCodeRequestAuthenticationConverter implements AuthenticationConverter {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeRequestAuthenticationConverter.class);
    private final OAuth2AuthorizationCodeRequestAuthenticationConverter converter = new OAuth2AuthorizationCodeRequestAuthenticationConverter();
    public AuthorizationCodeRequestAuthenticationConverter() {
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        var original = (OAuth2AuthorizationCodeRequestAuthenticationToken) converter.convert(request);
        String prompt = (String) original.getAdditionalParameters().get("prompt");
        if ("login".equals(prompt)) {
            throw new LoginRequiredException("force login");
        }
        var additionalParams = new HashMap<>(original.getAdditionalParameters());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AuthTokenAuthenticationToken authToken) {
            String asid = authToken.getAuthSession().getId();
            log.debug("Find Auth Session {}. Save asid to authorization request parameters", authToken);
            additionalParams.put("asid", asid);
        }
        return new OAuth2AuthorizationCodeRequestAuthenticationToken(
            original.getAuthorizationUri(),
            original.getClientId(),
            (Authentication) original.getPrincipal(),
            original.getRedirectUri(),
            original.getState(),
            original.getScopes(),
            additionalParams
        );
    }
}
