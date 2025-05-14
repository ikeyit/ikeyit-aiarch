package com.ikeyit.gateway.auth;

import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Store the redirect url in the authorization request. After the authorization, it can be retrieved and redirect back to
 * the url
 */
public class RedirectServerAuthorizationRequestResolver extends DefaultServerOAuth2AuthorizationRequestResolver {

    public RedirectServerAuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        super(clientRegistrationRepository);
    }

    public RedirectServerAuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository, ServerWebExchangeMatcher authorizationRequestMatcher) {
        super(clientRegistrationRepository, authorizationRequestMatcher);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        return super.resolve(exchange).map(request -> {
            String redirectAfterAuth = exchange.getRequest().getQueryParams().getFirst("redirect");
            if (redirectAfterAuth == null) {
                return request;
            } else {
                // OAuth2AuthorizationRequest is immutable. We have to recreate a new one
                return OAuth2AuthorizationRequest.from(request)
                    .attributes(Map.of("redirect", redirectAfterAuth))
                    .build();
            }
        });

    }
}
