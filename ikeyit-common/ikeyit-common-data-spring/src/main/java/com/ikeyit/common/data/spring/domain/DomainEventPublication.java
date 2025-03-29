package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.domain.DomainEvent;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DomainEventPublication {
    private final String listenerId;
    private final DomainEvent event;
    private final Instant createdAt;
    private final Map<String, Object> headers;

    public DomainEventPublication(DomainEvent event, String listenerId) {
        this(event, listenerId, Instant.now().truncatedTo(ChronoUnit.MICROS));
    }

    public DomainEventPublication(DomainEvent event, String listenerId, Instant createdAt) {
        this(event, listenerId, createdAt, null);
    }

    public DomainEventPublication(DomainEvent event, String listenerId, Instant createdAt, Map<String, Object> headers) {
        Assert.notNull(event, "event should not be null!");
        Assert.notNull(listenerId, "listenerId should not be null!");
        Assert.notNull(createdAt, "createTime should not be null!");
        this.listenerId = listenerId;
        this.event = event;
        this.createdAt = createdAt;
        this.headers = headers == null || headers.isEmpty() ? new HashMap<>() : new HashMap<>(headers);
    }

    public UUID getEventId() {
        return event.getEventId();
    }

    public String getListenerId() {
        return listenerId;
    }

    public DomainEvent getEvent() {
        return event;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Object getHeader(String name) {
        return headers.get(name);
    }

    public Map<String, Object> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public void putHeader(String name, Object value) {
        headers.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEventPublication that)) return false;
        return Objects.equals(listenerId, that.listenerId) && Objects.equals(event, that.event) && Objects.equals(createdAt, that.createdAt) && Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listenerId, event, createdAt, headers);
    }
}
