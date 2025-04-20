package com.ikeyit.classroom.infrastructure.repository;

import com.ikeyit.classroom.domain.model.Teacher;
import com.ikeyit.classroom.domain.repository.TeacherRepository;
import com.ikeyit.common.data.domain.PublishDomainEvent;
import com.ikeyit.common.spring.jdbc.EnhancedRowMapper;
import com.ikeyit.common.spring.jdbc.EnhancedSqlParameterSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

import static com.ikeyit.common.spring.jdbc.JdbcTemplateSupport.createWithGeneratedKey;

@Repository
public class JdbcTeacherRepository implements TeacherRepository {
    private static final String SQL_INSERT = """
        INSERT INTO teacher
        (name, faculty, title, user_id, created_at, updated_at)
        VALUES
        (:name, :faculty, :title, :userId, :createdAt, :updatedAt)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM teacher WHERE id = :id
        """;

    private static final String SQL_FIND_BY_USER_ID = """
        SELECT * FROM teacher WHERE user_id = :userId
        """;

    private static final String SQL_UPDATE = """
        UPDATE teacher SET
        name = :name,
        faculty = :faculty,
        title = :title,
        updated_at = :updatedAt
        WHERE id = :id
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM teacher WHERE id = :id
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Teacher> rowMapper = new EnhancedRowMapper<>(Teacher.class);

    public JdbcTeacherRepository(@Qualifier("classroomJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(Teacher entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(Teacher entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(Teacher entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

    @Override
    public Optional<Teacher> findByUserId(Long userId) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_USER_ID, Map.of("userId", userId), rowMapper));
    }
}