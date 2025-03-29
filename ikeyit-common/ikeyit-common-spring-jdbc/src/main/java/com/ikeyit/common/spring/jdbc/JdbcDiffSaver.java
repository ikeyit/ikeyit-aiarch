package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.domain.Entity;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * A simple implementation to only save the changed parts of a list of entities.
 * The disadvantage is that the entities are updated even though they are not changed.
 * The better way is to use advanced ORM entity manager.
 * @param <A>
 * @param <ID>
 */
public interface JdbcDiffSaver <A extends Entity<ID>, ID>  {
    SqlParameterSource insertSqlParameterSource(A entity);
    default SqlParameterSource updateSqlParameterSource(A entity) {
        return insertSqlParameterSource(entity);
    }
    void doInsert(List<A> insertEntities, List<SqlParameterSource> sqlParameterSources);
    void doUpdate(List<A> updateEntities, List<SqlParameterSource> sqlParameterSources);
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