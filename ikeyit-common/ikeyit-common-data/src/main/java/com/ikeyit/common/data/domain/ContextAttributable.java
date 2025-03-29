package com.ikeyit.common.data.domain;

/**
 * Interface for objects that can store and retrieve context-specific attributes.
 * This allows for flexible storage of temporary or contextual data that doesn't belong
 * in the domain model but is needed for processing or cross-cutting concerns.
 */
public interface ContextAttributable {
    /**
     * Stores a context attribute with the specified name and value.
     *
     * @param name The name of the attribute to store
     * @param value The value to associate with the name
     */
    void putContextAttribute(String name, Object value);

    /**
     * Retrieves a context attribute by its name.
     *
     * @param name The name of the attribute to retrieve
     * @return The value associated with the name, or null if not found
     */
    Object getContextAttribute(String name);
}
