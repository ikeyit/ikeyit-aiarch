package com.ikeyit.common.data.domain;

/**
 * Marks a class as a domain entity with a unique identifier.
 * An entity is a domain object that has a distinct identity that runs through time and different states.
 * 
 * @param <ID> The type of the identifier for this entity
 */
public interface Entity<ID> {
    /**
     * Gets the unique identifier of this entity.
     * 
     * @return The entity's identifier
     */
    ID getId();

    /**
     * Assigns a new identifier to this entity.
     * This method is intended to be used by repositories only.
     * 
     * @param id The identifier to assign
     */
    @ForRepo
    void assignId(ID id);
}
