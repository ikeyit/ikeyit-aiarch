package com.ikeyit.classroom.infrastructure.repository;

import com.ikeyit.classroom.domain.model.ClassSchedule;
import com.ikeyit.classroom.domain.repository.ClassScheduleRepository;
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
public class JdbcClassScheduleRepository implements ClassScheduleRepository {
    private static final String SQL_INSERT = """
        INSERT INTO class_schedule
        (course_id, classroom, day_of_week, start_time, end_time, created_at, updated_at)
        VALUES
        (:courseId, :classroom, :dayOfWeek, :startTime, :endTime, :createdAt, :updatedAt)
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT * FROM class_schedule WHERE id = :id
        """;

    private static final String SQL_FIND_BY_COURSE_ID = """
        SELECT * FROM class_schedule WHERE course_id = :courseId
        """;
        
    private static final String SQL_FIND_BY_CLASSROOM = """
        SELECT * FROM class_schedule WHERE classroom = :classroom
        """;
        
    private static final String SQL_FIND_BY_DAY_OF_WEEK = """
        SELECT * FROM class_schedule WHERE day_of_week = :dayOfWeek
        """;

    private static final String SQL_DELETE_BY_ID = """
        DELETE FROM class_schedule WHERE id = :id
        """;

    private static final String SQL_UPDATE = """
        UPDATE class_schedule SET
        classroom = :classroom,
        day_of_week = :dayOfWeek,
        start_time = :startTime,
        end_time = :endTime,
        updated_at = :updatedAt
        WHERE id = :id
        """;
        
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<ClassSchedule> rowMapper = new EnhancedRowMapper<>(ClassSchedule.class);

    public JdbcClassScheduleRepository(@Qualifier("classroomJdbcTemplate")
                               NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ClassSchedule> findById(Long id) {
        return DataAccessUtils.optionalResult(jdbcTemplate.query(SQL_FIND_BY_ID, Map.of("id", id), rowMapper));
    }

    @Override
    @PublishDomainEvent
    public void create(ClassSchedule entity) {
        createWithGeneratedKey(jdbcTemplate, SQL_INSERT, entity, EnhancedSqlParameterSource::new);
    }

    @Override
    public List<ClassSchedule> findByCourseId(Long courseId) {
        return jdbcTemplate.query(SQL_FIND_BY_COURSE_ID, Map.of("courseId", courseId), rowMapper);
    }

    @Override
    public List<ClassSchedule> findByClassroom(String classroom) {
        return jdbcTemplate.query(SQL_FIND_BY_CLASSROOM, Map.of("classroom", classroom), rowMapper);
    }

    @Override
    public List<ClassSchedule> findByDayOfWeek(Integer dayOfWeek) {
        return jdbcTemplate.query(SQL_FIND_BY_DAY_OF_WEEK, Map.of("dayOfWeek", dayOfWeek), rowMapper);
    }

    @Override
    @PublishDomainEvent
    public void update(ClassSchedule entity) {
        jdbcTemplate.update(SQL_UPDATE, new EnhancedSqlParameterSource(entity));
    }

    @Override
    @PublishDomainEvent
    public void delete(ClassSchedule entity) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, Map.of("id", entity.getId()));
    }

}