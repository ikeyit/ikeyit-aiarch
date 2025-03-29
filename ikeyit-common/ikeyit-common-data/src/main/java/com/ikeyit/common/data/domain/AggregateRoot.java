package com.ikeyit.common.data.domain;

/**
 * Marks a class as an aggregate root in Domain-Driven Design.
 * An aggregate root is the main entity of an aggregate and ensures the consistency of changes
 * being made within the aggregate boundary. It acts as a gateway to all other entities within its aggregate.
 * 
 * @param <ID> The type of the identifier for this aggregate root
 */
public interface AggregateRoot<ID> extends Entity<ID> {

}
