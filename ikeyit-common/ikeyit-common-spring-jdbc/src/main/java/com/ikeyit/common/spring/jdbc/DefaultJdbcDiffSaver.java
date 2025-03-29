package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.domain.Entity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Find the difference between the current entity collections and the previous, and then execute SQL for inserting, updating,
 * and deleting in batch.
 * @param <T>
 */
public class DefaultJdbcDiffSaver<ID, T extends Entity<ID>> implements JdbcDiffSaver<T, ID> {

    protected final NamedParameterJdbcTemplate jdbcTemplate;
    protected final String insertSql;
    protected final String updateSql;
    protected final String deleteSql;
    protected final boolean generateKey;

    public DefaultJdbcDiffSaver(NamedParameterJdbcTemplate jdbcTemplate, String insertSql, String updateSql, String deleteSql) {
        this(jdbcTemplate, insertSql, updateSql, deleteSql, true);
    }

    public DefaultJdbcDiffSaver(NamedParameterJdbcTemplate jdbcTemplate, String insertSql, String updateSql, String deleteSql, boolean generateKey) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertSql = insertSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
        this.generateKey = generateKey;
    }

    @Override
    public SqlParameterSource insertSqlParameterSource(T entity) {
        return new EnhancedSqlParameterSource(entity);
    }

    @Override
    public void doInsert(List<T> insertEntities, List<SqlParameterSource> sqlParameterSources) {
        if (generateKey) {
            JdbcTemplateSupport.batchCreateWithGeneratedKey(jdbcTemplate, insertSql, insertEntities,
                sqlParameterSources.toArray(SqlParameterSource[]::new));
        } else {
            jdbcTemplate.batchUpdate(insertSql, sqlParameterSources.toArray(SqlParameterSource[]::new));
        }
    }

    @Override
    public void doUpdate(List<T> updateEntities, List<SqlParameterSource> sqlParameterSources) {
        jdbcTemplate.batchUpdate(updateSql, sqlParameterSources.toArray(SqlParameterSource[]::new));
    }

    @Override
    public void doDelete(Set<ID> ids) {
        jdbcTemplate.update(deleteSql, Map.of("ids", ids));
    }
}
