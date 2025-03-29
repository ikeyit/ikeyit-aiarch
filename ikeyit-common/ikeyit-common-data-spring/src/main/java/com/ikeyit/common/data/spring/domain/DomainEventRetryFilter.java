package com.ikeyit.common.data.spring.domain;

/**
 * Interface for filters that can intercept and process domain event retry operations.
 * This is part of a filter chain pattern that allows for customized handling of domain event retries.
 */
public interface DomainEventRetryFilter {
    /**
     * Processes a domain event retry operation and passes control to the next filter in the chain.
     *
     * @param listener The listener that will handle the event
     * @param domainEventPublication The domain event publication to be processed
     * @param filterChain The chain of filters to continue processing
     */
    void doFilter(PersistTransactionalApplicationListener listener, DomainEventPublication domainEventPublication, DomainEventRetryFilterChain filterChain);

}
