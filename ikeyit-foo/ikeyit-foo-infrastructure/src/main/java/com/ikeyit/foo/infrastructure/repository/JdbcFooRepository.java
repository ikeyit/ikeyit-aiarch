package com.ikeyit.foo.infrastructure.repository;

import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
import com.ikeyit.foo.domain.model.Foo;
import com.ikeyit.foo.domain.repository.FooRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

/**
 * <pre>
 * === AI-NOTE ===
 * - Use NamedJdbcTemplate and utilities in com.ikeyit.common.spring.jdbc to implement the repository
 * - Use text block to define SQL
 * - Use @PublishDomainEvent to publish domain events when the model is saved to database
 * === AI-NOTE-END ===
 * </pre>
 * A jdbc implementation for FooRepository
 */
@Repository
public abstract class JdbcFooRepository implements FooRepository {
    // AI-NOTE: Use static text block to define SQL if it is not dynamic
    private static final String SQL_INSERT = """
        INSERT INTO foo
        (message, source, created_at, updated_at)
        VALUES
        (:message, :source, :createdAt, :updatedAt)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM store WHERE id = :id
        """;

    private static final String SQL_FIND_ALL = """
        SELECT * FROM store
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM foo WHERE id = :id
        """;

    private static final String SQL_UPDATE = """
        UPDATE store SET
        message=:message,
        source=:source,
        updated_at=:updatedAt
        WHERE id=:id
        """;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    // AI-NOTE: Use EnhancedRowMapper to map data. It can handle EnumWithInt fields and @JsonField fields automatically
    private final RowMapper<Foo> rowMapper = new EnhancedRowMapper<>(Foo.class);

    public JdbcFooRepository(@Qualifier("fooJdbcTemplate")
                               NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Foo> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    public List<Foo> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
    }

    /**
     * <pre>
     * === AI-NOTE ===
     * - Use @PublishDomainEvent to publish domain events when the model is saved to database
     * === AI-NOTE-END ===
     * </pre>
     */
    @Override
    @PublishDomainEvent
    public void create(Foo entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    /**
     * <pre>
     * === AI-NOTE ===
     * - Use @PublishDomainEvent to publish domain events when the model is saved to database
     * === AI-NOTE-END ===
     * </pre>
     */
    @Override
    @PublishDomainEvent
    public void update(Foo entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    /**
     * <pre>
     * === AI-NOTE ===
     * - Use @PublishDomainEvent to publish domain events when the model is saved to database
     * === AI-NOTE-END ===
     * </pre>
     */
    @Override
    @PublishDomainEvent
    public void delete(Foo entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

}
