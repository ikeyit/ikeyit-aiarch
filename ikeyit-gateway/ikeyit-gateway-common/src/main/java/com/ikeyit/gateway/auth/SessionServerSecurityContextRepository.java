package com.ikeyit.gateway.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

/**
 * Implementation of ServerSecurityContextRepository that stores the SecurityContext in the user's session.
 * Provides a method to update the security context in the same session without changing the session ID.
 */
public class SessionServerSecurityContextRepository implements ServerSecurityContextRepository {
    private static final Logger log = LoggerFactory.getLogger(SessionServerSecurityContextRepository.class);

    private final String springSecurityContextAttrName = "securityContext";

    private boolean cacheSecurityContext;

    /**
     * If set to true the result of {@link #load(ServerWebExchange)} will use
     * {@link Mono#cache()} to prevent multiple lookups.
     * @param cacheSecurityContext true if {@link Mono#cache()} should be used, else
     * false.
     */
    public void setCacheSecurityContext(boolean cacheSecurityContext) {
        this.cacheSecurityContext = cacheSecurityContext;
    }

    /**
     * Updates the SecurityContext in the user's session.
     * <p>
     * This method updates the security context in the session without changing the session ID.
     * It's useful for updating the security context during the same session.
     *
     * @param exchange The server web exchange
     * @param context The security context to update (must not be null)
     * @return A Mono that completes when the security context has been updated
     */
    public Mono<Void> update(ServerWebExchange exchange, SecurityContext context) {
        Assert.notNull(context, "SecurityContext must not be null");
        return exchange.getSession().doOnNext(session -> {
            session.getAttributes().put(this.springSecurityContextAttrName, context);
            log.debug("Updated security context: {}", context);
        }).then();
    }

    /**
     * Saves the SecurityContext in the user's session and changes the session ID.
     * <p>
     * This method stores the security context in the session and changes the session ID,
     * which is a security best practice when authentication state changes (e.g., login).
     * If the context is null, it removes the security context from the session.
     *
     * @param exchange The server web exchange
     * @param context The security context to save (can be null to remove the context)
     * @return A Mono that completes when the security context has been saved and session ID changed
     */
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return exchange.getSession().doOnNext(session -> {
            if (context == null) {
                session.getAttributes().remove(this.springSecurityContextAttrName);
                log.debug("Removed security context from session: {}", context);
            }
            else {
                session.getAttributes().put(this.springSecurityContextAttrName, context);
                log.debug("Saved security context: {}", context);
            }
        }).flatMap(WebSession::changeSessionId);
    }

    /**
     * Loads the SecurityContext from the user's session.
     * <p>
     * This method retrieves the security context from the session. If caching is enabled,
     * the result will be cached to prevent multiple lookups of the same security context.
     *
     * @param exchange The server web exchange
     * @return A Mono that emits the security context, or an empty Mono if not found
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        Mono<SecurityContext> result = exchange.getSession().flatMap((session) -> {
            SecurityContext context = session.getAttribute(this.springSecurityContextAttrName);
            log.debug("Loaded security context from session: {}", context);
            return Mono.justOrEmpty(context);
        });
        return (this.cacheSecurityContext) ? result.cache() : result;
    }

}
