package com.ikeyit.access.infrastructure.repository;

import com.ikeyit.access.domain.model.SystemRole;
import com.ikeyit.access.domain.repository.SystemRoleRepository;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

@Repository
public class JdbcSystemRoleRepository implements SystemRoleRepository {
    private static final String SQL_INSERT = """
        INSERT INTO system_role (name, permission_ids, realm_type, default_role, supreme)
        VALUES (:name, :permissionIds, :realmType, :defaultRole, :supreme)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM system_role WHERE id = :id
        """;

    private static final String SQL_FIND_BY_REALM_IDS = """
        SELECT * FROM system_role WHERE realm_type = :realmType AND id in (:ids)
        """;

    private static final String SQL_FIND_BY_REALM = """
        SELECT * FROM system_role WHERE realm_type = :realmType ORDER BY id ASC
        """;
    private static final String SQL_FIND_BY_REALM_DEFAULT = """
        SELECT * FROM system_role WHERE realm_type = :realmType AND default_role = TRUE
        """;
    private static final String SQL_FIND_BY_REALM_SUPREME = """
        SELECT * FROM system_role WHERE realm_type = :realmType AND supreme = TRUE
        """;
    private static final String SQL_FIND_BY_REALM_NAME = """
        SELECT * FROM system_role WHERE name = :name AND realm_type = :realmType
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM system_role WHERE id = :id
        """;

    private static final String SQL_UPDATE = """
        UPDATE system_role SET
        name=:name,
        permission_ids=:permissionIds,
        realm_type=:realmType,
        default_role=:defaultRole,
        supreme = :supreme
        WHERE id=:id
        """;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<SystemRole> rowMapper = new EnhancedRowMapper<>(SystemRole.class);

    @Autowired
    public JdbcSystemRoleRepository(@Qualifier("accessGlobalJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<SystemRole> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID,  Map.of("id", id), rowMapper));
    }

    @Override
    public List<SystemRole> findByRealmAndIds(String realmType, Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return List.of();
        return jdbcTemplate.query(SQL_FIND_BY_REALM_IDS, Map.of("ids", ids, "realmType", realmType), rowMapper);
    }

    @Override
    public Map<Long, SystemRole> findByRealmAndIdsAsMap(String realmType, Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return Map.of();
        return findByRealmAndIds(realmType, ids).stream().collect(Collectors.toMap(SystemRole::getId, Function.identity()));
    }

    @Override
    public List<SystemRole> findByRealm(String realmType) {
        return jdbcTemplate.query(SQL_FIND_BY_REALM, Map.of("realmType", realmType), rowMapper);
    }

    @Override
    public Optional<SystemRole> findByRealmAndName(String realmType, String name) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_REALM_NAME,
            Map.of("name", name, "realmType", realmType),
            rowMapper));
    }

    @Override
    public Optional<SystemRole> findByRealmAndId(String realmType, Long id) {
        return findById(id).filter(item -> Objects.equals(realmType, item.getRealmType()));
    }

    @Override
    public Optional<SystemRole> findDefault(String realmType) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_REALM_DEFAULT,  Map.of("realmType", realmType), rowMapper));
    }

    @Override
    public Optional<SystemRole> findSupreme(String realmType) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_REALM_SUPREME,  Map.of("realmType", realmType), rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(SystemRole systemRole) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, systemRole, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(SystemRole systemRole) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(systemRole));
    }

    @Override
    @PublishDomainEvent
    public void delete(SystemRole entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

}
