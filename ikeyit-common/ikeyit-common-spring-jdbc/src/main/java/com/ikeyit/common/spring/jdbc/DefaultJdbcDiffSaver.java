package com.ikeyit.common.spring.jdbc;

import com.ikeyit.common.data.domain.Entity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of JdbcDiffSaver that handles batch operations for entity collections.
 * This class provides functionality to compare current and previous entity collections,
 * then executes appropriate SQL statements for inserting, updating, and deleting in batch.
 *
 * @param <ID> the type of entity identifier
 * @param <T> the type of entity being managed
 */
public class DefaultJdbcDiffSaver<ID, T extends Entity<ID>> implements JdbcDiffSaver<T, ID> {

    /** The JDBC template used for database operations */
    protected final NamedParameterJdbcTemplate jdbcTemplate;
    /** SQL statement for inserting new entities */
    protected final String insertSql;
    /** SQL statement for updating existing entities */
    protected final String updateSql;
    /** SQL statement for deleting entities */
    protected final String deleteSql;
    /** Flag indicating whether to generate keys for new entities */
    protected final boolean generateKey;

    /**
     * Creates a new DefaultJdbcDiffSaver with key generation enabled by default.
     *
     * @param jdbcTemplate the JDBC template to use for database operations
     * @param insertSql the SQL statement for inserting entities
     * @param updateSql the SQL statement for updating entities
     * @param deleteSql the SQL statement for deleting entities
     */
    public DefaultJdbcDiffSaver(NamedParameterJdbcTemplate jdbcTemplate, String insertSql, String updateSql, String deleteSql) {
        this(jdbcTemplate, insertSql, updateSql, deleteSql, true);
    }

    /**
     * Creates a new DefaultJdbcDiffSaver with specified key generation setting.
     *
     * @param jdbcTemplate the JDBC template to use for database operations
     * @param insertSql the SQL statement for inserting entities
     * @param updateSql the SQL statement for updating entities
     * @param deleteSql the SQL statement for deleting entities
     * @param generateKey whether to generate keys for new entities
     */
    public DefaultJdbcDiffSaver(NamedParameterJdbcTemplate jdbcTemplate, String insertSql, String updateSql, String deleteSql, boolean generateKey) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertSql = insertSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
        this.generateKey = generateKey;
    }

    /**
     * Creates a SqlParameterSource for inserting a new entity using EnhancedSqlParameterSource.
     *
     * @param entity the entity to create parameters for
     * @return SqlParameterSource containing the entity's insert parameters
     */
    @Override
    public SqlParameterSource insertSqlParameterSource(T entity) {
        return new EnhancedSqlParameterSource(entity);
    }

    /**
     * Performs batch insert operations for new entities, optionally generating keys.
     *
     * @param insertEntities list of entities to insert
     * @param sqlParameterSources corresponding SQL parameters for each entity
     */
    @Override
    public void doInsert(List<T> insertEntities, List<SqlParameterSource> sqlParameterSources) {
        if (generateKey) {
            JdbcTemplateSupport.batchCreateWithGeneratedKey(jdbcTemplate, insertSql, insertEntities,
                sqlParameterSources.toArray(SqlParameterSource[]::new));
        } else {
            jdbcTemplate.batchUpdate(insertSql, sqlParameterSources.toArray(SqlParameterSource[]::new));
        }
    }

    /**
     * Performs batch update operations for existing entities.
     *
     * @param updateEntities list of entities to update
     * @param sqlParameterSources corresponding SQL parameters for each entity
     */
    @Override
    public void doUpdate(List<T> updateEntities, List<SqlParameterSource> sqlParameterSources) {
        jdbcTemplate.batchUpdate(updateSql, sqlParameterSources.toArray(SqlParameterSource[]::new));
    }

    /**
     * Performs a batch delete operation for entities by their IDs.
     *
     * @param ids set of entity IDs to delete
     */
    @Override
    public void doDelete(Set<ID> ids) {
        jdbcTemplate.update(deleteSql, Map.of("ids", ids));
    }
}
