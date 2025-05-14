package com.ikeyit.access.infrastructure.repository;


import com.ikeyit.access.domain.model.Permission;
import com.ikeyit.access.domain.model.PermissionGroup;
import com.ikeyit.access.domain.repository.PermissionGroupRepository;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

@Repository
public class JdbcPermissionGroupRepository implements PermissionGroupRepository {
    private static final String SQL_INSERT = """
        INSERT INTO permission_group (name) VALUES (:name)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM permission_group WHERE id = :id
        """;

    private static final String SQL_FIND_BY_IDS = """
        SELECT * FROM permission_group WHERE id in (:ids)
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM permission_group WHERE id = :id
        """;

    private static final String SQL_UPDATE = """
        UPDATE permission_group SET
        name=:name
        WHERE id=:id
        """;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<PermissionGroup> rowMapper = new EnhancedRowMapper<>(PermissionGroup.class);

    @Autowired
    public JdbcPermissionGroupRepository(@Qualifier("accessGlobalJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PermissionGroup> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID,  Map.of("id", id), rowMapper));
    }

    @Override
    public List<PermissionGroup> findByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return List.of();
        return jdbcTemplate.query(SQL_FIND_BY_IDS, Map.of("ids", ids), rowMapper);
    }

    @Override
    public Map<Long, PermissionGroup> findByIdsAsMap(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return Map.of();
        return findByIds(ids).stream().collect(Collectors.toMap(PermissionGroup::getId, Function.identity()));
    }

    @Override
    @PublishDomainEvent
    public void create(PermissionGroup entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(PermissionGroup entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(PermissionGroup entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

}
