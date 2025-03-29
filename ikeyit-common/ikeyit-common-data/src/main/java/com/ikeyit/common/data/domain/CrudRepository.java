package com.ikeyit.common.data.domain;

import java.util.Optional;

/**
 * Generic interface for CRUD (Create, Read, Update, Delete) operations on a repository.
 * 
 * @param <T> The domain type the repository manages
 * @param <ID> The type of the identifier of the domain type
 */
public interface CrudRepository<T, ID> {
    /**
     * Retrieves an entity by its identifier.
     *
     * @param id The identifier of the entity to retrieve
     * @return An Optional containing the found entity or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Creates a new entity in the repository.
     *
     * @param entity The entity to create
     */
    void create(T entity);

    /**
     * Updates an existing entity in the repository.
     *
     * @param entity The entity to update
     */
    void update(T entity);

    /**
     * Deletes an entity from the repository.
     *
     * @param entity The entity to delete
     */
    void delete(T entity);
}
