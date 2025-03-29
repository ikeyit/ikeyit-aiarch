package com.ikeyit.common.data.spring.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DomainEventRepository {
    void save(DomainEventPublication domainEventPublication);
    void delete(UUID eventId, String listenerId);
    List<DomainEventPublication> findBefore(Instant time, int maxCount);
}
