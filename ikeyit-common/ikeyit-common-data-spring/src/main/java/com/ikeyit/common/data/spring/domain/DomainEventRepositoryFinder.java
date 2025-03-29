package com.ikeyit.common.data.spring.domain;

import java.util.List;

/**
 * Interface for finding all available domain event repositories in the system.
 * This is typically used in multi-tenant or multi-schema environments where domain events
 * are stored in different repositories or database schemas.
 */
public interface DomainEventRepositoryFinder {
    /**
     * Finds all available domain event repositories in the system.
     *
     * @return A list of all domain event repositories
     */
    List<DomainEventRepository> findAll();
}
