package com.ikeyit.common.data.spring.domain;

import org.springframework.transaction.PlatformTransactionManager;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * A manager class that coordinates multiple domain event managers across different repositories.
 * This class provides centralized management for domain events across multiple storage backends,
 * facilitating event retry mechanisms and coordinated event processing.
 */
public class MultiDomainEventManager {
    private final DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory;

    private final DomainEventRepositoryFinder domainEventRepositoryFinder;

    private final List<DomainEventRetryFilter> retryFilters;

    private final PlatformTransactionManager platformTransactionManager;

    /**
     * Constructs a new MultiDomainEventManager.
     *
     * @param domainTransactionalEventListenerFactory Factory for creating transactional event listeners
     * @param domainEventRepositoryFinder Finder for locating domain event repositories
     * @param platformTransactionManager Transaction manager for handling event processing
     * @param retryFilters List of filters to control event retry behavior
     */
    public MultiDomainEventManager(DomainTransactionalEventListenerFactory domainTransactionalEventListenerFactory,
                                   DomainEventRepositoryFinder domainEventRepositoryFinder,
                                   PlatformTransactionManager platformTransactionManager,
                                   List<DomainEventRetryFilter> retryFilters) {
        this.domainTransactionalEventListenerFactory = domainTransactionalEventListenerFactory;
        this.domainEventRepositoryFinder = domainEventRepositoryFinder;
        this.platformTransactionManager = platformTransactionManager;
        this.retryFilters = Objects.requireNonNullElseGet(retryFilters, List::of);
    }

    /**
     * Retrieves all domain event managers across different repositories.
     *
     * @return List of domain event managers, one for each repository
     */
    public List<DomainEventManager> getAllManagers() {

        return domainEventRepositoryFinder.findAll().stream()
            .map(r -> new DomainEventManager(domainTransactionalEventListenerFactory, platformTransactionManager, r, retryFilters))
            .toList();
    }

    /**
     * Retries failed events across all domain event managers.
     *
     * @param duration The time window for selecting events to retry
     * @param maxCount The maximum number of events to retry per manager
     */
    public void retryEvents(Duration duration, int maxCount) {
        for (DomainEventManager domainEventManager : getAllManagers()) {
            domainEventManager.retryEvents(duration, maxCount);
        }
    }
}
