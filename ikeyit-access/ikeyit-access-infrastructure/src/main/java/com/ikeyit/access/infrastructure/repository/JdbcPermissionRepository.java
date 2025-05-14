package com.ikeyit.access.infrastructure.repository;


import com.ikeyit.access.domain.model.Permission;
import com.ikeyit.access.domain.repository.PermissionRepository;
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
public class JdbcPermissionRepository implements PermissionRepository {
    private static final String SQL_INSERT = """
        INSERT INTO permission (name, description, realm_type) VALUES (:name, :description, :realmType)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM permission WHERE id = :id
        """;

    private static final String SQL_FIND_BY_IDS = """
        SELECT * FROM permission WHERE id in (:ids)
        """;

    private static final String SQL_FIND_BY_REALM_TYPE = """
        SELECT * FROM permission WHERE realm_type = :realmType
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM permission WHERE id = :id
        """;

    private static final String SQL_UPDATE = """
        UPDATE permission SET
        name=:name,
        description=:description,
        realm_type=:realmType
        WHERE id=:id
        """;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Permission> rowMapper = new EnhancedRowMapper<>(Permission.class);

    @Autowired
    public JdbcPermissionRepository(@Qualifier("accessGlobalJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID,  Map.of("id", id), rowMapper));
    }

    @Override
    public List<Permission> findByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return List.of();
        return jdbcTemplate.query(SQL_FIND_BY_IDS, Map.of("ids", ids), rowMapper);
    }

    @Override
    public Map<Long, Permission> findByIdsAsMap(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return Map.of();
        return findByIds(ids).stream().collect(Collectors.toMap(Permission::getId, Function.identity()));
    }

    @Override
    public List<Permission> findByRealmType(String realmType) {
        return jdbcTemplate.query(SQL_FIND_BY_REALM_TYPE, Map.of("realmType", realmType), rowMapper);
    }

    @Override
    @PublishDomainEvent
    public void create(Permission permission) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, permission, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(Permission permission) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(permission));
    }

    @Override
    @PublishDomainEvent
    public void delete(Permission entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

}
