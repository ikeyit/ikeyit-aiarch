package com.ikeyit.gateway.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveRefreshTokenTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

/**
 * Service responsible for managing OAuth2 token refresh operations in a reactive environment.
 * 
 * This service handles the automatic refreshing of OAuth2 access tokens when they are near expiration,
 * ensuring continuous authentication without disrupting the user experience. It works with Spring Security's
 * OAuth2 client support and is designed for reactive applications using WebFlux.
 */
public class TokenRefreshService {

    private static final Logger log = LoggerFactory.getLogger(TokenRefreshService.class);

    /**
     * Repository for storing and retrieving OAuth2 authorized client information.
     * Used to access current token information and store refreshed tokens.
     */
    private final ServerOAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    /**
     * Repository for managing security context in a session-based environment.
     * Used to update the security context with refreshed authentication information.
     */
    private final SessionServerSecurityContextRepository securityContextRepository;

    /**
     * Client for making OAuth2 refresh token requests to the authorization server.
     * Handles the HTTP communication for token refresh operations.
     */
    private final WebClientReactiveRefreshTokenTokenResponseClient refreshTokenTokenResponseClient;

    /**
     * Factory for creating JWT decoders based on client registration information.
     * Used to decode ID tokens received during the refresh process.
     */
    private final JwtDecoderFactory<ClientRegistration> jwtDecoderFactory;

    /**
     * Time window before token expiration when a refresh should be attempted.
     * Default is 5 minutes before expiration.
     */
    private Duration refreshWindow = Duration.ofMinutes(5);

    /**
     * Constructs a new TokenRefreshService with the required dependencies.
     *
     * @param oAuth2AuthorizedClientRepository Repository for OAuth2 authorized client information
     * @param securityContextRepository Repository for security context management
     * @param jwtDecoderFactory Factory for creating JWT decoders
     */
    public TokenRefreshService(ServerOAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository,
                               SessionServerSecurityContextRepository securityContextRepository,
                               JwtDecoderFactory<ClientRegistration> jwtDecoderFactory) {
        this.oAuth2AuthorizedClientRepository = oAuth2AuthorizedClientRepository;
        this.securityContextRepository = securityContextRepository;
        this.jwtDecoderFactory = jwtDecoderFactory;
        this.refreshTokenTokenResponseClient = new WebClientReactiveRefreshTokenTokenResponseClient();
    }

    /**
     * Sets the time window before token expiration when a refresh should be attempted.
     *
     * @param refreshWindow The duration before token expiration to trigger a refresh
     */
    public void setRefreshWindow(Duration refreshWindow) {
        Assert.notNull(refreshWindow, "refreshWindow must not be null");
        this.refreshWindow = refreshWindow;
    }

    /**
     * Checks if the current access token is near expiration and refreshes it if necessary.
     * This method is typically called automatically by the RefreshTokenFilter during request processing.
     *
     * @param authentication The current OAuth2 authentication token
     * @param exchange The current server web exchange
     * @return A Mono that completes when the check and potential refresh operation is done
     */
    public Mono<Void> checkAndRefreshToken(OAuth2AuthenticationToken authentication, ServerWebExchange exchange) {

        return oAuth2AuthorizedClientRepository.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication, exchange)
            .flatMap(authorizedClient -> {
                var accessToken = authorizedClient.getAccessToken();
                if (isAccessTokenNearExpiration(accessToken)) {
                    log.debug("Access token is near expiration, attempting to refresh");
                    return doRefresh(authorizedClient, authentication, exchange);
                }
                log.debug("No need to refresh access token");
                return Mono.empty();
            });
    }

    /**
     * Forces a token refresh regardless of the current token's expiration status.
     * This can be used when a token refresh is explicitly requested by the application.
     *
     * @param authentication The current OAuth2 authentication token
     * @param exchange The current server web exchange
     * @return A Mono that completes when the refresh operation is done
     * @throws AuthenticationCredentialsNotFoundException if authentication information is missing
     */
    public Mono<Void> refreshToken(OAuth2AuthenticationToken authentication, ServerWebExchange exchange) {
        return oAuth2AuthorizedClientRepository
            .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication, exchange)
            .switchIfEmpty(Mono.error(new AuthenticationCredentialsNotFoundException("Authentication required")))
            .flatMap(authorizedClient -> doRefresh(authorizedClient, authentication, exchange));
    }


    private boolean isAccessTokenNearExpiration(OAuth2AccessToken accessToken) {
        Instant now = Instant.now();
        Instant expiresAt = accessToken.getExpiresAt();
        return expiresAt != null && now.plus(refreshWindow).isAfter(expiresAt);
    }

    private Mono<Void> doRefresh(OAuth2AuthorizedClient authorizedClient,
                                                      OAuth2AuthenticationToken authentication,
                                                      ServerWebExchange exchange) {
        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
        if (refreshToken == null) {
            return Mono.error(new AuthenticationCredentialsNotFoundException("Authentication required"));
        }

        var request = new OAuth2RefreshTokenGrantRequest(authorizedClient.getClientRegistration(),
            authorizedClient.getAccessToken(),
            authorizedClient.getRefreshToken());

        return refreshTokenTokenResponseClient.getTokenResponse(request)
            .flatMap(tokenResponse ->
                    updateAuthorizedClient(authorizedClient, authentication, exchange, tokenResponse));
    }

    private Mono<Void> updateAuthorizedClient(
        OAuth2AuthorizedClient authorizedClient,
        OAuth2AuthenticationToken authentication,
        ServerWebExchange exchange,
        OAuth2AccessTokenResponse tokenResponse) {
        // Create a new authorized client with the refreshed tokens
        var newAuthorizedClient = new OAuth2AuthorizedClient(
            authorizedClient.getClientRegistration(),
            authorizedClient.getPrincipalName(),
            tokenResponse.getAccessToken(),
            tokenResponse.getRefreshToken());
        
        // Extract and decode the ID token from the response
        String idTokenValue = (String) tokenResponse.getAdditionalParameters().get("id_token");
        Jwt jwt = jwtDecoderFactory
            .createDecoder(authorizedClient.getClientRegistration())
            .decode(idTokenValue);
        OidcIdToken idToken = new OidcIdToken(
            jwt.getTokenValue(),
            jwt.getIssuedAt(),
            jwt.getExpiresAt(),
            jwt.getClaims());
        
        // Create a new OIDC user with the updated ID token
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        var newOidcUser = new DefaultOidcUser(oidcUser.getAuthorities(), idToken);
        
        // Create a new authentication token with the updated user
        var newAuthentication = new OAuth2AuthenticationToken(newOidcUser, authentication.getAuthorities(), authentication.getAuthorizedClientRegistrationId());
        
        // Create a new security context with the updated authentication
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(newAuthentication);
        
        // Save the updated authorized client and security context
        return oAuth2AuthorizedClientRepository
            .saveAuthorizedClient(newAuthorizedClient, authentication, exchange)
            .then(securityContextRepository.update(exchange, securityContext))
            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }
}