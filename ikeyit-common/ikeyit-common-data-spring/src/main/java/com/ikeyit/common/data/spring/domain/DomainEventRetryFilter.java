package com.ikeyit.common.data.spring.domain;

public interface DomainEventRetryFilter {
    void doFilter(PersistTransactionalApplicationListener listener, DomainEventPublication domainEventPublication, DomainEventRetryFilterChain filterChain);

}
