package com.ikeyit.common.data.domain;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Interface for objects that can supply domain events.
 * This is typically implemented by aggregate roots and other domain objects that need to
 * communicate state changes or important occurrences within the domain.
 */
public interface DomainEventSupplier {
    /**
     * Adds a domain event to the collection of events.
     *
     * @param domainEvent The domain event to add
     */
    void addDomainEvent(DomainEvent domainEvent);

    /**
     * Clears all domain events from the collection.
     * This is typically called after all events have been processed.
     */
    void clearDomainEvents();

    /**
     * Returns all domain events that have been collected.
     *
     * @return A non-null list of domain events
     */
    @Nonnull
    List<DomainEvent> domainEvents();
}
