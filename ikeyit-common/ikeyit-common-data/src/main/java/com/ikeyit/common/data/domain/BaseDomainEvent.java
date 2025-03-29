package com.ikeyit.common.data.domain;

import java.util.Objects;
import java.util.UUID;

public class BaseDomainEvent implements DomainEvent {
    protected UUID eventId;

    protected BaseDomainEvent() {
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public void assignEventId() {
        if (this.eventId == null) {
            this.eventId = UUID.randomUUID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseDomainEvent that)) return false;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventId);
    }
}
