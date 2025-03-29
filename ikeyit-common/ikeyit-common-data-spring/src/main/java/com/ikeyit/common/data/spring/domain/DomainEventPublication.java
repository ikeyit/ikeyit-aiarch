package com.ikeyit.common.data.spring.domain;

import com.ikeyit.common.data.domain.DomainEvent;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents a publication of a domain event to a specific listener.
 * This class encapsulates a domain event along with metadata about when it was created,
 * which listener should process it, and any additional headers or context information.
 */
public class DomainEventPublication {
    /**
     * The identifier of the listener that should process this event.
     */
    private final String listenerId;
    
    /**
     * The domain event being published.
     */
    private final DomainEvent event;
    
    /**
     * The timestamp when this publication was created.
     */
    private final Instant createdAt;
    
    /**
     * Additional headers or context information for this publication.
     */
    private final Map<String, Object> headers;

    /**
     * Constructs a new domain event publication with the current time as creation timestamp.
     *
     * @param event The domain event to publish
     * @param listenerId The identifier of the listener that should process this event
     */
    public DomainEventPublication(DomainEvent event, String listenerId) {
        this(event, listenerId, Instant.now().truncatedTo(ChronoUnit.MICROS));
    }

    /**
     * Constructs a new domain event publication with a specified creation timestamp.
     *
     * @param event The domain event to publish
     * @param listenerId The identifier of the listener that should process this event
     * @param createdAt The timestamp when this publication was created
     */
    public DomainEventPublication(DomainEvent event, String listenerId, Instant createdAt) {
        this(event, listenerId, createdAt, null);
    }

    /**
     * Constructs a new domain event publication with a specified creation timestamp and headers.
     *
     * @param event The domain event to publish
     * @param listenerId The identifier of the listener that should process this event
     * @param createdAt The timestamp when this publication was created
     * @param headers Additional headers or context information for this publication
     * @throws IllegalArgumentException if event, listenerId, or createdAt is null
     */
    public DomainEventPublication(DomainEvent event, String listenerId, Instant createdAt, Map<String, Object> headers) {
        Assert.notNull(event, "event should not be null!");
        Assert.notNull(listenerId, "listenerId should not be null!");
        Assert.notNull(createdAt, "createTime should not be null!");
        this.listenerId = listenerId;
        this.event = event;
        this.createdAt = createdAt;
        this.headers = headers == null || headers.isEmpty() ? new HashMap<>() : new HashMap<>(headers);
    }

    /**
     * Gets the unique identifier of the event.
     *
     * @return The UUID of the event
     */
    public UUID getEventId() {
        return event.getEventId();
    }

    /**
     * Gets the identifier of the listener that should process this event.
     *
     * @return The listener identifier
     */
    public String getListenerId() {
        return listenerId;
    }

    /**
     * Gets the domain event being published.
     *
     * @return The domain event
     */
    public DomainEvent getEvent() {
        return event;
    }

    /**
     * Gets the timestamp when this publication was created.
     *
     * @return The creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets a header value by name.
     *
     * @param name The name of the header to retrieve
     * @return The header value, or null if not found
     */
    public Object getHeader(String name) {
        return headers.get(name);
    }

    /**
     * Gets all headers as an unmodifiable map.
     *
     * @return An unmodifiable view of all headers
     */
    public Map<String, Object> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * Adds or updates a header with the specified name and value.
     *
     * @param name The name of the header
     * @param value The value to associate with the header
     */
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
