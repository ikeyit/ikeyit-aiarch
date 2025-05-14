package com.ikeyit.access.infrastructure.repository;

import com.ikeyit.access.domain.model.Org;
import com.ikeyit.access.domain.repository.OrgRepository;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

@Repository
public class JdbcOrgRepository implements OrgRepository {
    private static final String SQL_INSERT = """
        INSERT INTO org (
        name, picture, created_at, updated_at)
        VALUES (
        :name, :picture, :createdAt, :updatedAt)
        """;
    private static final String SQL_UPDATE = """
        UPDATE org SET
        name=:name,
        picture=:picture,
        updated_at=:updatedAt
        WHERE id=:id
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM org WHERE id = :id
        """;
    private static final String SQL_FIND = """
        SELECT * FROM org;
        """;
    private static final String SQL_DELETE = """
        DELETE FROM org WHERE id = :id;
        """;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Org> rowMapper = new EnhancedRowMapper<>(Org.class);

    @Autowired
    public JdbcOrgRepository(@Qualifier("accessJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Org> find() {
        List<Org> records = jdbcTemplate.query(SQL_FIND, rowMapper);
        return DataAccessUtils.optionalResult(records);
    }

    @Override
    public Optional<Org> findById(Long id) {
        List<Org> records = jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper);
        return DataAccessUtils.optionalResult(records);
    }

    @Override
    @PublishDomainEvent
    public void create(Org entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(Org entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(Org entity) {
        jdbcTemplate.update(SQL_DELETE, Map.of("id", entity.getId()));
    }
}
