package com.ikeyit.common.data.spring.domain;

import org.springframework.util.Assert;

import java.util.List;


public class DomainEventRetryFilterChain {

    private final List<DomainEventRetryFilter> filters;

    private int currentPosition = 0;

    private final int size;

    public DomainEventRetryFilterChain(List<DomainEventRetryFilter> filters) {
        Assert.notEmpty(filters, "Filters are empty");
        this.filters = filters;
        this.size = filters.size();
    }

    public void doFilter(PersistTransactionalApplicationListener listener, DomainEventPublication domainEventPublication) {
        if (currentPosition == size) {
            return;
        }
        currentPosition++;
        DomainEventRetryFilter nextFilter = filters.get(currentPosition - 1);
        nextFilter.doFilter(listener, domainEventPublication, this);
    }
}
