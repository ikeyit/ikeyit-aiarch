package com.ikeyit.access.infrastructure.repository;

import com.ikeyit.access.domain.model.CustomRole;
import com.ikeyit.access.domain.repository.CustomRoleRepository;
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
public class JdbcCustomRoleRepository implements CustomRoleRepository {
    private static final String SQL_INSERT = """
        INSERT INTO custom_role
        (name, permission_ids, realm_type, realm_id)
        VALUES
        (:name, :permissionIds, :realmType, :realmId)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM custom_role WHERE id = :id
        """;

    private static final String SQL_FIND_BY_IDS = """
        SELECT * FROM custom_role WHERE id in (:ids)
        """;

    private static final String SQL_FIND_BY_REALM = """
        SELECT * FROM custom_role WHERE realm_type = :realmType AND realm_id = :realmId
        """;
    private static final String SQL_FIND_BY_NAME_REALM = """
        SELECT * FROM custom_role WHERE name=:name AND realm_type = :realmType AND realm_id = :realmId
        """;


    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM custom_role WHERE id = :id
        """;

    private static final String SQL_UPDATE = """
        UPDATE custom_role SET
        name=:name,
        permission_ids=:permissionIds,
        realm_type=:realmType,
        realm_id=:realmId
        WHERE id=:id
        """;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<CustomRole> rowMapper = new EnhancedRowMapper<>(CustomRole.class);

    @Autowired
    public JdbcCustomRoleRepository(@Qualifier("accessJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<CustomRole> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID,  Map.of("id", id), rowMapper));
    }

    @Override
    public List<CustomRole> findByRealmAndIds(String realmType, Long realmId, Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return List.of();
        return jdbcTemplate.query(SQL_FIND_BY_IDS, Map.of("ids", ids,"realmType", realmType, "realmId", realmId), rowMapper);
    }

    @Override
    public Map<Long, CustomRole> findByRealmAndIdsAsMap(String realmType, Long realmId, Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return Map.of();
        return findByRealmAndIds(realmType, realmId, ids).stream()
            .collect(Collectors.toMap(CustomRole::getId, Function.identity()));
    }

    @Override
    public List<CustomRole> findByRealm(String realmType, Long realmId) {
        return jdbcTemplate.query(SQL_FIND_BY_REALM,
            Map.of("realmType", realmType, "realmId", realmId),
            rowMapper);
    }

    @Override
    public Optional<CustomRole> findByRealmAndName(String realmType, Long realmId, String name) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_NAME_REALM,
            Map.of(
                "name", name,
                "realmType", realmType,
                "realmId", realmId),
            rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(CustomRole systemRole) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, systemRole, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(CustomRole systemRole) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(systemRole));
    }

    @Override
    @PublishDomainEvent
    public void delete(CustomRole entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

}
