package com.ikeyit.classroom.infrastructure.repository;

import com.ikeyit.classroom.domain.model.Enrollment;
import com.ikeyit.classroom.domain.model.EnrollmentStatus;
import com.ikeyit.classroom.domain.repository.EnrollmentRepository;
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
public class JdbcEnrollmentRepository implements EnrollmentRepository {
    private static final String SQL_INSERT = """
        INSERT INTO enrollment
        (student_user_id, course_id, status, enroll_date, created_at, updated_at)
        VALUES
        (:studentUserId, :courseId, :status, :enrollDate, :createdAt, :updatedAt)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM enrollment WHERE id = :id
        """;

    private static final String SQL_FIND_BY_STUDENT_USER_ID = """
        SELECT * FROM enrollment WHERE student_user_id = :studentUserId
        """;
        
    private static final String SQL_FIND_BY_COURSE_ID = """
        SELECT * FROM enrollment WHERE course_id = :courseId
        """;
        
    private static final String SQL_FIND_BY_STATUS = """
        SELECT * FROM enrollment WHERE status = :status
        """;
        
    private static final String SQL_FIND_BY_STUDENT_USER_ID_AND_COURSE_ID = """
        SELECT * FROM enrollment WHERE student_user_id = :studentUserId AND course_id = :courseId
        """;

    private static final String SQL_UPDATE = """
        UPDATE enrollment SET
        status = :status,
        updated_at = :updatedAt
        WHERE id = :id
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM enrollment WHERE id = :id
        """;


    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Enrollment> rowMapper = new EnhancedRowMapper<>(Enrollment.class);

    public JdbcEnrollmentRepository(@Qualifier("classroomJdbcTemplate")
                               NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(Enrollment entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(Enrollment entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(Enrollment entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

    @Override
    public List<Enrollment> findByStudentUserId(Long studentUserId) {
        return jdbcTemplate.query(SQL_FIND_BY_STUDENT_USER_ID, Map.of("studentUserId", studentUserId), rowMapper);
    }

    @Override
    public List<Enrollment> findByCourseId(Long courseId) {
        return jdbcTemplate.query(SQL_FIND_BY_COURSE_ID, Map.of("courseId", courseId), rowMapper);
    }

    @Override
    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        return jdbcTemplate.query(SQL_FIND_BY_STATUS, Map.of("status", status.name()), rowMapper);
    }

    @Override
    public Optional<Enrollment> findByStudentUserIdAndCourseId(Long studentUserId, Long courseId) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(
                SQL_FIND_BY_STUDENT_USER_ID_AND_COURSE_ID,
                Map.of("studentUserId", studentUserId, "courseId", courseId),
                rowMapper));
    }
}