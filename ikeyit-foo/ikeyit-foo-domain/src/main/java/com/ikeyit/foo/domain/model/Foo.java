package com.ikeyit.foo.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.foo.domain.event.FooCreatedEvent;
import com.ikeyit.foo.domain.event.FooUpdatedEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Foo domain model representing a foo in the system. It's an aggregate root.
 * NOTE:
 * Aggregate root should extend BaseAggregateRoot
 * Domain model should be rich model.
 * Define the concrete business methods to modify the model and keep data consistency.
 * Do not define the setters to directly update the inner fields. It may destroy the data consistency.
 */
public class Foo extends BaseAggregateRoot<Long> {
    private Long id;
    private String message;
    private FooStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * This construct is used for the persistence
     * DO NOT use it in your business logic
     */
    @ForRepo
    protected Foo() {
    }

    public Foo(String message) {
        this.message = message;
        this.status = FooStatus.ACTIVE;
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        this.updatedAt = createdAt;
        // Generate a domain event to notify the domain model is created
        // Use lazy method to initialize a domain event, because the ID is not generated at the moment。
        addDomainEvent(() -> new FooCreatedEvent(this));
    }


    /**
     * This is your business method.
     * Change the message
     * @param message
     */
    public void changeMessage(String message) {
        this.message = message;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        // Generate a domain event to notify it is updated
        addDomainEvent(new FooUpdatedEvent(this));
    }

    public String getMessage() {
        return message;
    }

    public FooStatus getStatus() {
        return status;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Assign an id for a new entity. It is used for the persistence
     * DO NOT use it in your business logic
     * @param id The identifier to assign
     */
    @Override
    @ForRepo
    public void assignId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
