package com.ikeyit.common.data.spring.domain;

import org.springframework.util.Assert;

import java.util.List;

/**
 * Chain of responsibility implementation for domain event retry filters.
 * This class manages the execution of multiple filters in sequence when retrying domain events.
 */
public class DomainEventRetryFilterChain {

    /**
     * The list of filters to be applied in sequence.
     */
    private final List<DomainEventRetryFilter> filters;

    /**
     * The current position in the filter chain.
     */
    private int currentPosition = 0;

    /**
     * The total number of filters in the chain.
     */
    private final int size;

    /**
     * Constructs a new filter chain with the specified filters.
     *
     * @param filters The list of filters to be applied in sequence
     * @throws IllegalArgumentException if the filters list is empty
     */
    public DomainEventRetryFilterChain(List<DomainEventRetryFilter> filters) {
        Assert.notEmpty(filters, "Filters are empty");
        this.filters = filters;
        this.size = filters.size();
    }

    /**
     * Executes the next filter in the chain.
     * If all filters have been executed, this method returns without doing anything.
     *
     * @param listener The listener that will handle the event
     * @param domainEventPublication The domain event publication to be processed
     */
    public void doFilter(PersistTransactionalApplicationListener listener, DomainEventPublication domainEventPublication) {
        if (currentPosition == size) {
            return;
        }
        currentPosition++;
        DomainEventRetryFilter nextFilter = filters.get(currentPosition - 1);
        nextFilter.doFilter(listener, domainEventPublication, this);
    }
}
