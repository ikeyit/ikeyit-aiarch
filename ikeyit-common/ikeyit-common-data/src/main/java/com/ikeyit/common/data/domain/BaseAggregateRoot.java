package com.ikeyit.common.data.domain;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class BaseAggregateRoot<ID> implements AggregateRoot<ID>, DomainEventSupplier, ContextAttributable, Entity<ID> {

    private transient final List<Object> domainEvents = new ArrayList<>();

    private transient Map<String, Object> contextAttributes;

    public void addDomainEvent(DomainEvent domainEvent) {
        domainEvents.add(domainEvent);
    }

    // Useful when the domain event need to be constructed later
    public void addDomainEvent(Supplier<DomainEvent> domainEventSupplier) {
        domainEvents.add(domainEventSupplier);
    }


    public void clearDomainEvents() {
        domainEvents.clear();
    }

    @Nonnull
    public List<DomainEvent> domainEvents() {
        return domainEvents.stream().map(this::mapDomainEvent).toList();
    }

    @SuppressWarnings("unchecked")
    private DomainEvent mapDomainEvent(Object obj) {
        if (obj instanceof DomainEvent domainEvent)
            return domainEvent;

        return ((Supplier<DomainEvent>) obj).get();
    }

    @Override
    public void putContextAttribute(String name, Object value) {
        if (contextAttributes == null) {
            contextAttributes = new HashMap<>();
        }
        contextAttributes.put(name, value);
    }

    @Override
    public Object getContextAttribute(String name) {
        if (contextAttributes == null) {
            return null;
        }

        return contextAttributes.get(name);
    }
}
