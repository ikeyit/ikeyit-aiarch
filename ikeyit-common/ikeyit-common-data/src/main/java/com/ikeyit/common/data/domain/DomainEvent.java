package com.ikeyit.common.data.domain;

import java.util.UUID;

public interface DomainEvent {
    UUID getEventId();
    void assignEventId();
}
