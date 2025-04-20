package com.ikeyit.classroom.infrastructure.repository;

import com.ikeyit.classroom.domain.model.Course;
import com.ikeyit.classroom.domain.repository.CourseRepository;
import com.ikeyit.common.data.domain.PublishDomainEvent;
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
public class JdbcCourseRepository implements CourseRepository {
    private static final String SQL_INSERT = """
        INSERT INTO course
        (name, description, image_url, credits, teacher_user_id, start_date, end_date, status, created_at, updated_at)
        VALUES
        (:name, :description, :imageUrl, :credits, :teacherUserId, :startDate, :endDate, :status, :createdAt, :updatedAt)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM course WHERE id = :id
        """;

    private static final String SQL_FIND_ALL = """
        SELECT * FROM course
        """;
        
    private static final String SQL_FIND_BY_TEACHER_USER_ID = """
        SELECT * FROM course WHERE teacher_user_id = :teacherUserId
        """;

    private static final String SQL_UPDATE = """
        UPDATE course SET
        name = :name,
        description = :description,
        image_url = :imageUrl,
        credits = :credits,
        start_date = :startDate,
        end_date = :endDate,
        status = :status,
        updated_at = :updatedAt
        WHERE id = :id
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM course WHERE id = :id
        """;


    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Course> rowMapper = new EnhancedRowMapper<>(Course.class);

    public JdbcCourseRepository(@Qualifier("classroomJdbcTemplate")
                               NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Course> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(Course entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(Course entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(Course entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
    }

    @Override
    public List<Course> findByTeacherUserId(Long teacherUserId) {
        return jdbcTemplate.query(SQL_FIND_BY_TEACHER_USER_ID, Map.of("teacherUserId", teacherUserId), rowMapper);
    }
}