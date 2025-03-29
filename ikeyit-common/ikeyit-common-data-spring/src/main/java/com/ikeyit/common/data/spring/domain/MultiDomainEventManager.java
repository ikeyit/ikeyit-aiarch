package com.ikeyit.common.data.spring.domain;

import org.springframework.transaction.PlatformTransactionManager;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class MultiDomainEventManager {
    private final DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory;

    private final DomainEventRepositoryFinder domainEventRepositoryFinder;

    private final List<DomainEventRetryFilter> retryFilters;

    private final PlatformTransactionManager platformTransactionManager;

    public MultiDomainEventManager(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory,
                                   DomainEventRepositoryFinder domainEventRepositoryFinder,
                                   PlatformTransactionManager platformTransactionManager,
                                   List<DomainEventRetryFilter> retryFilters) {
        this.domainTransactionalEventListenerFactory = domainTransactionalEventListenerFactory;
        this.domainEventRepositoryFinder = domainEventRepositoryFinder;
        this.platformTransactionManager = platformTransactionManager;
        this.retryFilters = Objects.requireNonNullElseGet(retryFilters, List::of);
    }

    public List<DomainEventManager> getAllManagers() {

        return domainEventRepositoryFinder.findAll().stream()
            .map(r -> new DomainEventManager(domainTransactionalEventListenerFactory, platformTransactionManager, r, retryFilters))
            .toList();
    }

    public void retryEvents(Duration duration, int maxCount) {
        for (DomainEventManager domainEventManager : getAllManagers()) {
            domainEventManager.retryEvents(duration, maxCount);
        }
    }
}
