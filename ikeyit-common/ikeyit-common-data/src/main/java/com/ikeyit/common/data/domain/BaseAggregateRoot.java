package com.ikeyit.common.data.domain;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Base implementation of an aggregate root that provides domain event handling and context attributes management.
 * This class serves as a foundation for creating aggregate roots in the domain model.
 * 
 * @param <ID> The type of the identifier for this aggregate root
 */
public abstract class BaseAggregateRoot<ID> implements AggregateRoot<ID>, DomainEventSupplier, ContextAttributable {

    private transient final List<Object> domainEvents = new ArrayList<>();

    private transient Map<String, Object> contextAttributes;

    /**
     * Adds a domain event to this aggregate root.
     * The event will be collected and can be processed later.
     *
     * @param domainEvent The domain event to add
     */
    public void addDomainEvent(DomainEvent domainEvent) {
        domainEvents.add(domainEvent);
    }

    /**
     * Adds a domain event supplier to this aggregate root.
     * The actual domain event will be constructed later when needed.
     * This is useful for lazy event construction.
     *
     * @param domainEventSupplier A supplier that will provide the domain event
     */
    public void addDomainEvent(Supplier<DomainEvent> domainEventSupplier) {
        domainEvents.add(domainEventSupplier);
    }

    /**
     * Clears all domain events from this aggregate root.
     * This is typically called after all events have been processed.
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }

    /**
     * Returns all domain events associated with this aggregate root.
     * If an event was added as a supplier, it will be constructed at this point.
     *
     * @return A list of domain events
     */
    @Nonnull
    public List<DomainEvent> domainEvents() {
        return domainEvents.stream().map(this::mapDomainEvent).toList();
    }

    private DomainEvent mapDomainEvent(Object obj) {
        if (obj instanceof DomainEvent domainEvent)
            return domainEvent;
        if (obj instanceof Supplier<?> supplier)
            return (DomainEvent) supplier.get();
        throw new IllegalArgumentException("Invalid domain event object: " + obj);
    }

    /**
     * Stores a context attribute with the specified name and value.
     * Creates the context attributes map if it doesn't exist.
     *
     * @param name The name of the attribute
     * @param value The value to store
     */
    @Override
    public void putContextAttribute(String name, Object value) {
        if (contextAttributes == null) {
            contextAttributes = new HashMap<>();
        }
        contextAttributes.put(name, value);
    }

    /**
     * Retrieves a context attribute by name.
     *
     * @param name The name of the attribute to retrieve
     * @return The attribute value, or null if not found or if no attributes exist
     */
    @Override
    public Object getContextAttribute(String name) {
        if (contextAttributes == null) {
            return null;
        }

        return contextAttributes.get(name);
    }
}
