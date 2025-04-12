package com.ikeyit.account.infrastructure.repository;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.account.domain.repository.UserRepository;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

/**
 * JDBC implementation of the UserRepository
 */
@Repository
public class JdbcUserRepository implements UserRepository {
    public static final String SQL_INSERT = """
            INSERT INTO user_info
            (password,username,email,phone,display_name,avatar,gender,location,enabled,verified,roles,locale)
            VALUES
            (:password,:username,:email,:phone,:displayName,:avatar,:gender,:location,:enabled,:verified,:roles,:locale)
            """;
    private static final String SQL_UPDATE = """
            UPDATE user_info SET
            password=:password,
            phone=:phone,
            username=:username,
            email=:email,
            display_name=:displayName,
            avatar=:avatar,
            gender=:gender,
            location=:location,
            roles=:roles,
            locale=:locale
            WHERE id=:id
            """;

    private static final String SQL_FIND_BY_USERNAME = """
            SELECT * FROM user_info WHERE username = :username
            """;
    private static final String SQL_FIND_BY_ID = """
            SELECT * FROM user_info WHERE id = :id
            """;
    private static final String SQL_FIND_BY_EMAIL = """
            SELECT * FROM user_info WHERE email = :email
            """;
    private static final String SQL_FIND_BY_MOBILE = """
            SELECT * FROM user_info WHERE phone = :phone
            """;

    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM user_info WHERE id = :id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = new EnhancedRowMapper<>(User.class);

    @Autowired
    public JdbcUserRepository(@Qualifier("accountJdbcTemplate")
                              NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @PublishDomainEvent
    public void create(User user) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, user, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(User user) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(user));
    }

    @Override
    @PublishDomainEvent
    public void delete(User entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_USERNAME, Map.of("username", username), rowMapper));
    }

    @Override
    public Optional<User> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_EMAIL, Map.of("email", email), rowMapper));
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_MOBILE, Map.of("phone", phone), rowMapper));
    }
} 