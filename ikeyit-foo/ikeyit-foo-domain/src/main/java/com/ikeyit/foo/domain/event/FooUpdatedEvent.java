package com.ikeyit.foo.domain.event;

import com.ikeyit.common.data.domain.BaseDomainEvent;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.foo.domain.model.Foo;

import java.time.Instant;

public class FooUpdatedEvent extends BaseDomainEvent {
    private Long id;
    private String message;
    private Instant createdAt;
    private Instant updatedAt;

    @ForRepo
    FooUpdatedEvent() {
    }

    public FooUpdatedEvent(Foo foo) {
        this.id = foo.getId();
        this.message = foo.getMessage();
        this.createdAt = foo.getCreatedAt();
        this.updatedAt = foo.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "FooUpdatedEvent{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
