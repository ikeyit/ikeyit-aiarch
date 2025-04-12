package com.ikeyit.account.infrastructure.repository;

import com.ikeyit.account.domain.model.UserConnection;
import com.ikeyit.account.domain.repository.UserConnectionRepository;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
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
public class JdbcUserConnectionRepository implements UserConnectionRepository {

    private static final String SQL_INSERT = """
        INSERT INTO user_connection
        (local_user_id,provider,sub,
        name,preferred_username,nickname,
        picture, email, phone_number)
        VALUES
        (:localUserId,:provider,:sub,
        :name,:preferredUsername,:nickname,
        :picture, :email, :phoneNumber)
        """;
    private static final String SQL_UPDATE = """
        UPDATE user_connection SET
        local_user_id=:localUserId,
        name=:name,
        preferred_username=:preferredUsername,
        nickname=:nickname,
        picture=:picture,
        email=:email,
        phone_number=:phoneNumber
        WHERE provider=:provider AND sub=:sub
        """;
    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM user_connection WHERE id = :id
        """;

    private static final String SQL_FIND_BY_LOCAL_USER_ID = """
        SELECT * FROM user_connection WHERE local_user_id=:localUserId
        """;

    private static final String SQL_FIND_BY_LOCAL_USER_ID_AND_PROVIDER = """
        SELECT * FROM user_connection WHERE local_user_id=:localUserId AND provider=:provider
        """;

    private static final String SQL_FIND_BY_PROVIDER_SUB = """
        SELECT * FROM user_connection WHERE provider=:provider AND sub=:sub
        """;

    private static final String SQL_DELETE = """
        DELETE FROM user_connection WHERE local_user_id=:localUserId AND provider=:provider AND sub=:sub
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<UserConnection> rowMapper = new EnhancedRowMapper<>(UserConnection.class);

    public JdbcUserConnectionRepository(@Qualifier("accountJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserConnection> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    public void create(UserConnection userConnection) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, userConnection, EnhancedSqlParameterSource::new);
    }

    @Override
    public void update(UserConnection userConnection) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(userConnection));
    }

    @Override
    public void delete(UserConnection entity) {
    }

    @Override
    public List<UserConnection> findByLocalUserId(Long localUserId) {
        return jdbcTemplate.query(SQL_FIND_BY_LOCAL_USER_ID,
            Map.of("localUserId", localUserId),
            rowMapper);
    }

    @Override
    public List<UserConnection> findByLocalUserIdAndProvider(Long localUserId, String provider) {
        return jdbcTemplate.query(SQL_FIND_BY_LOCAL_USER_ID_AND_PROVIDER,
            Map.of("localUserId", localUserId, "provider", provider),
            rowMapper);
    }

    @Override
    public Optional<UserConnection> findByProviderSub(String provider, String sub) {
        List<UserConnection> records = jdbcTemplate.query(SQL_FIND_BY_PROVIDER_SUB,
            Map.of("provider", provider, "sub", sub),
            rowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(records));
    }


    public void delete(Long localUserId, String provider, String sub) {
        jdbcTemplate.update(SQL_DELETE,
            Map.of("localUserId", localUserId, "provider", provider,"sub", sub));
    }
}
