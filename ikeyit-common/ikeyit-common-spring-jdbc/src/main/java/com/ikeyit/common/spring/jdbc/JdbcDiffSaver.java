package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.domain.Entity;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Interface for managing batch operations on collections of entities using JDBC.
 * This implementation focuses on efficiency by only saving the changed parts of a list of entities.
 * While simpler than a full ORM solution, it provides basic differential update capabilities.
 * 
 * Note: The current implementation updates entities even if they haven't changed.
 * For more sophisticated change tracking, consider using an ORM entity manager.
 *
 * @param <A> the type of entity being managed
 * @param <ID> the type of the entity's identifier
 */
public interface JdbcDiffSaver <A extends Entity<ID>, ID>  {
    /**
     * Creates a SqlParameterSource for inserting a new entity.
     *
     * @param entity the entity to create parameters for
     * @return SqlParameterSource containing the entity's insert parameters
     */
    SqlParameterSource insertSqlParameterSource(A entity);

    /**
     * Creates a SqlParameterSource for updating an existing entity.
     * By default, uses the same parameter source as insert operations.
     *
     * @param entity the entity to create parameters for
     * @return SqlParameterSource containing the entity's update parameters
     */
    default SqlParameterSource updateSqlParameterSource(A entity) {
        return insertSqlParameterSource(entity);
    }

    /**
     * Performs batch insert operations for new entities.
     *
     * @param insertEntities list of entities to insert
     * @param sqlParameterSources corresponding SQL parameters for each entity
     */
    void doInsert(List<A> insertEntities, List<SqlParameterSource> sqlParameterSources);

    /**
     * Performs batch update operations for existing entities.
     *
     * @param updateEntities list of entities to update
     * @param sqlParameterSources corresponding SQL parameters for each entity
     */
    void doUpdate(List<A> updateEntities, List<SqlParameterSource> sqlParameterSources);

    /**
     * Performs batch delete operations for entities by their IDs.
     *
     * @param ids set of entity IDs to delete
     */
    void doDelete(Set<ID> ids);

    default void save(@Nonnull Collection<A> entities, @Nonnull Collection<ID> prevIds) {
        Set<ID> deleteIds = new HashSet<>(prevIds);
        List<A> insertEntities = new ArrayList<>();
        List<A> updateEntities = new ArrayList<>();
        List<SqlParameterSource> insertParameterSources = new ArrayList<>();
        List<SqlParameterSource> updateParameterSources = new ArrayList<>();
        for (A entity : entities) {
            if (entity.getId() == null) {
                insertEntities.add(entity);
                insertParameterSources.add(insertSqlParameterSource(entity));
            } else if (prevIds.contains(entity.getId())) {
                updateEntities.add(entity);
                updateParameterSources.add(updateSqlParameterSource(entity));
                deleteIds.remove(entity.getId());
            } else {
                throw new IllegalStateException("Entity id is invalid!");
            }
        }
        if (!insertEntities.isEmpty()){
            doInsert(insertEntities, insertParameterSources);
        }
        if (!updateEntities.isEmpty()) {
            doUpdate(updateEntities, updateParameterSources);
        }
        if (!deleteIds.isEmpty()) {
            doDelete(deleteIds);
        }
    }
}