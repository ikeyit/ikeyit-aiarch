package com.ikeyit.common.data.spring.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for persisting and retrieving domain events.
 * This interface defines the contract for storing, retrieving, and managing domain event publications.
 */
public interface DomainEventRepository {
    /**
     * Saves a domain event publication to the repository.
     *
     * @param domainEventPublication The domain event publication to save
     */
    void save(DomainEventPublication domainEventPublication);
    
    /**
     * Deletes a domain event publication from the repository.
     *
     * @param eventId The unique identifier of the event to delete
     * @param listenerId The identifier of the listener associated with the event
     */
    void delete(UUID eventId, String listenerId);
    
    /**
     * Finds domain event publications created before the specified time.
     *
     * @param time The instant before which events should be retrieved
     * @param maxCount The maximum number of events to retrieve
     * @return A list of domain event publications that match the criteria
     */
    List<DomainEventPublication> findBefore(Instant time, int maxCount);
}
