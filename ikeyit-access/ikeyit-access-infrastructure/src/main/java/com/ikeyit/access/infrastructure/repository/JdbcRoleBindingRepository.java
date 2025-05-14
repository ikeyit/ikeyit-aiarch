package com.ikeyit.access.infrastructure.repository;

import com.ikeyit.access.domain.model.RoleBinding;
import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.access.domain.repository.RoleBindingRepository;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
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

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

@Repository
public class JdbcRoleBindingRepository implements RoleBindingRepository {
    private static final String SQL_INSERT = """
        INSERT INTO role_binding
        (user_id, role_id, role_type, realm_id, realm_type)
        VALUES
        (:userId, :roleId, :roleType, :realmId, :realmType)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM role_binding WHERE id = :id
        """;

    private static final String SQL_FIND_BY_USER = """
        SELECT * FROM role_binding WHERE user_id=:userId AND realm_id=:realmId AND realm_type=:realmType
        """;

    private static final String SQL_FIND_BY_USER_ROLE = """
        SELECT * FROM role_binding WHERE
        user_id=:userId
        AND role_id=:roleId
        AND role_type=:roleType
        AND realm_id=:realmId
        AND realm_type=:realmType
        """;

    private static final String SQL_FIND_BY_USER_IDS_IN_REALM = """
        SELECT * FROM role_binding WHERE
        realm_type=:realmType
        AND realm_id=:realmId
        AND user_id IN (:userIds)
        """;
    private static final String SQL_FIND_BY_REALM_ROLE = """
        SELECT * FROM role_binding WHERE
        realm_type=:realmType
        AND realm_id=:realmId
        AND role_type=:roleType
        AND role_id=:roleId
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM role_binding WHERE id = :id
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<RoleBinding> rowMapper = new EnhancedRowMapper<>(RoleBinding.class);

    public JdbcRoleBindingRepository(@Qualifier("accessJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Optional<RoleBinding> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    public List<RoleBinding> findByUserIdInRealm(String realmType, Long realmId, Long userId) {
        return jdbcTemplate.query(SQL_FIND_BY_USER, Map.of("userId", userId, "realmId", realmId, "realmType", realmType), rowMapper);
    }

    @Override
    public  Optional<RoleBinding> findByUserIdAndRoleIdInRealm(String realmType, Long realmId, Long userId, RoleType roleType, Long roleId) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_USER_ROLE,
            Map.of(
                "userId", userId,
                "roleId", roleId,
                "roleType", roleType.value(),
                "realmType", realmType,
                "realmId", realmId),
            rowMapper));
    }

    @Override
    public List<RoleBinding> findByUserIdsInRealm(String realmType, Long realmId, Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds))
            return List.of();
        return jdbcTemplate.query(SQL_FIND_BY_USER_IDS_IN_REALM, Map.of("realmType", realmType, "realmId", realmId, "userIds", userIds), rowMapper);
    }

    @Override
    public List<RoleBinding> findByRoleAndRealm(String realmType, Long realmId, RoleType roleType, Long roleId) {
        return jdbcTemplate.query(SQL_FIND_BY_REALM_ROLE,
            Map.of("realmType", realmType, "realmId", realmId,  "roleType", roleType.value(),"roleId", roleId), rowMapper);
    }

    @Override
    @PublishDomainEvent
    public void create(RoleBinding entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    public void update(RoleBinding entity) {
        throw new IllegalArgumentException("Not implemented!");
    }

    @Override
    @PublishDomainEvent
    public void delete(RoleBinding entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }
}
