package com.ikeyit.common.data.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Base implementation of a domain event that provides common functionality for all domain events.
 * This class handles the event identifier management and implements standard object methods.
 */
public class BaseDomainEvent implements DomainEvent {
    protected UUID eventId;

    /**
     * Protected constructor for creating a new domain event.
     * The event ID is not assigned in the constructor and must be assigned later using assignEventId().
     */
    protected BaseDomainEvent() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getEventId() {
        return eventId;
    }

    /**
     * {@inheritDoc}
     * This implementation assigns a random UUID if one hasn't been assigned yet.
     */
    @Override
    public void assignEventId() {
        if (this.eventId == null) {
            this.eventId = UUID.randomUUID();
        }
    }

    /**
     * Compares this domain event with another object for equality.
     * Two domain events are considered equal if they have the same event ID.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseDomainEvent that)) return false;
        return Objects.equals(eventId, that.eventId);
    }

    /**
     * Returns a hash code value for this domain event based on its event ID.
     *
     * @return A hash code value for this domain event
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(eventId);
    }
}
