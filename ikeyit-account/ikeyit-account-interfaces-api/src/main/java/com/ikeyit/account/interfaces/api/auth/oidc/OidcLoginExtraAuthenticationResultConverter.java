package com.ikeyit.account.interfaces.api.auth.oidc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class OidcLoginExtraAuthenticationResultConverter implements Converter<OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken> {
    @Override
    public OAuth2AuthenticationToken convert(OAuth2LoginAuthenticationToken authenticationResult) {
        String redirectAfterAuth = authenticationResult
            .getAuthorizationExchange()
            .getAuthorizationRequest()
            .getAttribute("redirect");

        if (redirectAfterAuth != null) {
            ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            if (servletRequestAttributes != null) {
                servletRequestAttributes.setAttribute("redirect_after_auth", redirectAfterAuth, RequestAttributes.SCOPE_REQUEST);
            }
        }
        return new OAuth2AuthenticationToken(authenticationResult.getPrincipal(), authenticationResult.getAuthorities(),
            authenticationResult.getClientRegistration().getRegistrationId());
    }
}
