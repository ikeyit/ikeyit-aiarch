package com.ikeyit.gateway.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Implementation of ServerOAuth2AuthorizedClientRepository that stores OAuth2 authorized client information
 * in the user's session. Only one authorized client is stored per session.
 */
public class SessionServerOAuth2AuthorizedClientRepository implements ServerOAuth2AuthorizedClientRepository {

    private static final Logger log = LoggerFactory.getLogger(SessionServerOAuth2AuthorizedClientRepository.class);

    private final String sessionAttributeName = "gatewayAuthorizedClient";

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    /**
     * Constructs a new SessionServerOAuth2AuthorizedClientRepository with the specified client registration repository.
     *
     * @param clientRegistrationRepository The repository used to find client registrations by ID
     */
    public SessionServerOAuth2AuthorizedClientRepository(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    /**
     * Loads an OAuth2 authorized client by the provided client registration identifier.
     * <p>
     * This method retrieves the authorized client from the user's session and reconstructs
     * the OAuth2AuthorizedClient object using the client registration information.
     *
     * @param clientRegistrationId The registration identifier of the client to load
     * @param principal The principal associated with the authorized client
     * @param exchange The server web exchange
     * @param <T> The type of the authorized client
     * @return A Mono that emits the authorized client, or an empty Mono if not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(String clientRegistrationId,
                                                                           Authentication principal, ServerWebExchange exchange) {

        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.notNull(exchange, "exchange cannot be null");
        return (Mono<T>) exchange.getSession()
            .flatMap(session ->
                getAuthorizedClient(clientRegistrationId, session));
    }

    private Mono<OAuth2AuthorizedClient> getAuthorizedClient(String clientRegistrationId, WebSession session) {
        log.debug("getAuthorizedClient: {}", clientRegistrationId);
        AuthorizedClientDO authorizedClientDO = session.getAttribute(sessionAttributeName);
        if (authorizedClientDO == null || !Objects.equals(clientRegistrationId, authorizedClientDO.getClientRegistrationId())) {
            return Mono.empty();
        }
        return clientRegistrationRepository.findByRegistrationId(clientRegistrationId)
            .map((clientRegistration) -> new OAuth2AuthorizedClient(
                clientRegistration,
                authorizedClientDO.getPrincipalName(),
                authorizedClientDO.getAccessToken(),
                authorizedClientDO.getRefreshToken()));
    }

    /**
     * Saves an OAuth2 authorized client in the user's session.
     * <p>
     * This method stores the essential information of the authorized client in the session
     * as an AuthorizedClientDO object, which can be later retrieved and reconstructed.
     *
     * @param authorizedClient The authorized client to save
     * @param principal The principal associated with the authorized client
     * @param exchange The server web exchange
     * @return A Mono that completes when the authorized client has been saved
     */
    @Override
    public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal,
                                           ServerWebExchange exchange) {
        Assert.notNull(authorizedClient, "authorizedClient cannot be null");
        Assert.notNull(exchange, "exchange cannot be null");
        return exchange.getSession()
            .doOnSuccess((session) -> {
                var authorizedClientDO = new AuthorizedClientDO(
                    authorizedClient.getClientRegistration().getRegistrationId(),
                    authorizedClient.getAccessToken(),
                    authorizedClient.getRefreshToken(),
                    principal.getName()
                );
                session.getAttributes().put(sessionAttributeName, authorizedClientDO);
                log.debug("Saved OAuth2AuthorizedClient: {}", authorizedClientDO);
            })
            .then(Mono.empty());
    }

    /**
     * Removes an OAuth2 authorized client from the user's session.
     * <p>
     * This method removes the authorized client information from the session,
     * effectively logging the user out of the OAuth2 provider.
     *
     * @param clientRegistrationId The registration identifier of the client to remove
     * @param principal The principal associated with the authorized client
     * @param exchange The server web exchange
     * @return A Mono that completes when the authorized client has been removed
     */
    @Override
    public Mono<Void> removeAuthorizedClient(String clientRegistrationId, Authentication principal,
                                             ServerWebExchange exchange) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.notNull(exchange, "exchange cannot be null");
        return exchange.getSession()
            .doOnSuccess((session) -> {
                session.getAttributes().remove(sessionAttributeName);
            })
            .then(Mono.empty());
    }
}
