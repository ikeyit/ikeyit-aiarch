package com.ikeyit.access.infrastructure.repository;

import com.ikeyit.access.domain.model.Member;
import com.ikeyit.access.domain.repository.MemberRepository;
import com.ikeyit.common.data.Page;
import com.ikeyit.common.data.PageParam;
import com.ikeyit.common.data.SortParam;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private static final String SQL_INSERT_AUTO = """
        INSERT INTO member (
        user_id, display_name, status,  created_at, updated_at)
        VALUES (
        :userId, :displayName, :status, :createdAt, :updatedAt)
        """;
    private static final String SQL_UPDATE = """
        UPDATE member SET
        display_name=:displayName,
        status=:status,
        updated_at=:updatedAt
        WHERE id=:id
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM member WHERE id = :id
        """;

    private static final String SQL_FIND_BY_USER_ID = """
        SELECT * FROM member WHERE user_id = :userId
        """;
    private static final String SQL_FIND = """
        SELECT * FROM member WHERE TRUE
        """;
    private static final String SQL_COUNT = """
        SELECT count(*) FROM member WHERE TRUE
        """;

    private static final String SQL_FIND_IN_REALM = """
        SELECT DISTINCT m.*
        FROM member m JOIN role_binding rb ON m.user_id = rb.user_id
        WHERE rb.realm_id = :realmId AND rb.realm_type = :realmType
        """;
    private static final String SQL_COUNT_IN_REALM = """
        SELECT COUNT(DISTINCT m.user_id)
        FROM member m JOIN role_binding rb ON m.user_id = rb.user_id
        WHERE rb.realm_id = :realmId AND rb.realm_type = :realmType
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Member> rowMapper = new EnhancedRowMapper<>(Member.class);

    public JdbcMemberRepository(@Qualifier("accessJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(Member entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT_AUTO, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(Member entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(Member entity) {

    }

    @Override
    public Optional<Member> findByUserId(Long userId) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(
                SQL_FIND_BY_USER_ID,
                Map.of("userId", userId),
                rowMapper));
    }

    @Override
    @SuppressWarnings("all")
    public Page<Member> find(String name, PageParam pageParam, SortParam sortParam) {
        var paramSource = new MapSqlParameterSource()
                .addValue("pageSize", pageParam.getPageSize())
                .addValue("offset", pageParam.getOffset());
        StringBuilder condition = new StringBuilder();
        if (StringUtils.hasLength(name)) {
            condition.append(" AND name LIKE %:name%");
            paramSource.addValue("name", name);
        }
        // TODO IMPLEMENT SORT
        String sql = SQL_FIND + condition + " ORDER BY id ASC LIMIT :pageSize OFFSET :offset";
        String countSql = SQL_COUNT + condition;
        long total = jdbcTemplate.queryForObject(countSql, paramSource, Long.class);
        List<Member> records = jdbcTemplate.query(sql, paramSource, rowMapper);
        return new Page<>(records, pageParam, total);
    }

    @Override
    @SuppressWarnings("all")
    public Page<Member> findInRealm(String realmType, Long realmId, String name, PageParam pageParam, SortParam sortParam) {
        var paramSource = new MapSqlParameterSource()
                .addValue("realmType", realmType)
                .addValue("realmId", realmId)
                .addValue("pageSize", pageParam.getPageSize())
                .addValue("offset", pageParam.getOffset());
        StringBuilder condition = new StringBuilder();
        if (StringUtils.hasLength(name)) {
            condition.append(" AND m.display_name LIKE :name");
            paramSource.addValue("name", "%" + name + "%");
        }
        // TODO IMPLEMENT SORT
        String sql = SQL_FIND_IN_REALM + condition + " ORDER BY m.id DESC LIMIT :pageSize OFFSET :offset";
        String countSql = SQL_COUNT_IN_REALM + condition;
        long total = jdbcTemplate.queryForObject(countSql, paramSource, Long.class);
        List<Member> records = jdbcTemplate.query(sql, paramSource, rowMapper);
        return new Page<>(records, pageParam, total);
    }
}
