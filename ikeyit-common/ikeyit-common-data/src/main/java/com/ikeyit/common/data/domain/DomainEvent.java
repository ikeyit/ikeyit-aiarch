package com.ikeyit.common.data.domain;

import java.util.UUID;

/**
 * Represents a domain event in the system.
 * Domain events are used to capture and communicate state changes or important occurrences within the domain model.
 * Each event has a unique identifier to ensure traceability and prevent duplicate processing.
 */
public interface DomainEvent {
    /**
     * Gets the unique identifier of this event.
     *
     * @return The UUID of this event
     */
    UUID getEventId();

    /**
     * Assigns a new unique identifier to this event.
     * This method should be called when the event is first created.
     */
    void assignEventId();
}
