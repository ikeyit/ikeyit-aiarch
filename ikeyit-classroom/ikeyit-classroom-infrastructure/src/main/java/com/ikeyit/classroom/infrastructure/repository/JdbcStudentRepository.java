package com.ikeyit.classroom.infrastructure.repository;

import com.ikeyit.classroom.domain.model.Student;
import com.ikeyit.classroom.domain.repository.StudentRepository;
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
public class JdbcStudentRepository implements StudentRepository {
    private static final String SQL_INSERT = """
        INSERT INTO student
        (name, student_no, faculty, class_name, user_id, created_at, updated_at)
        VALUES
        (:name, :studentNo, :faculty, :className, :userId, :createdAt, :updatedAt)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM student WHERE id = :id
        """;

    private static final String SQL_FIND_BY_USER_ID = """
        SELECT * FROM student WHERE user_id = :userId
        """;
        
    private static final String SQL_FIND_BY_STUDENT_ID = """
        SELECT * FROM student WHERE student_id = :studentNo
        """;

    private static final String SQL_UPDATE = """
        UPDATE student SET
        name = :name,
        faculty = :faculty,
        class_name = :className,
        updated_at = :updatedAt
        WHERE id = :id
        """;


    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM student WHERE id = :id
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Student> rowMapper = new EnhancedRowMapper<>(Student.class);

    public JdbcStudentRepository(@Qualifier("classroomJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Student> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(Student entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    @PublishDomainEvent
    public void update(Student entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(Student entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

    @Override
    public Optional<Student> findByUserId(Long userId) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_USER_ID, Map.of("userId", userId), rowMapper));
    }

    @Override
    public Optional<Student> findByStudentNo(String studentNo) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_STUDENT_ID, Map.of("studentNo", studentNo), rowMapper));
    }
}