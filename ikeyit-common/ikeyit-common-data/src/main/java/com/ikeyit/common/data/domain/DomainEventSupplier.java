package com.ikeyit.common.data.domain;

import javax.annotation.Nonnull;
import java.util.List;

public interface DomainEventSupplier {
    void addDomainEvent(DomainEvent domainEvent);
    void clearDomainEvents();
    @Nonnull
    List<DomainEvent> domainEvents();
}
