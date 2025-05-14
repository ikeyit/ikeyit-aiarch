package com.ikeyit.gateway.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationCodeAuthenticationTokenConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Retrieve the redirect url and set it to ServerWebExchange attribute. After authentication, it can be used
 * to redirect the request.
 */
public class AuthorizationCodeAuthenticationTokenConverter extends ServerOAuth2AuthorizationCodeAuthenticationTokenConverter {
    public AuthorizationCodeAuthenticationTokenConverter(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        super(clientRegistrationRepository);
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return super.convert(serverWebExchange).doOnSuccess(a -> {
            if (a instanceof OAuth2AuthorizationCodeAuthenticationToken token) {
                String redirectAfterAuth = token.getAuthorizationExchange()
                    .getAuthorizationRequest()
                    .getAttribute("redirect");
                if (redirectAfterAuth != null) {
                    serverWebExchange.getAttributes().put("redirect_after_auth", redirectAfterAuth);
                }
            }
        });
    }
}
