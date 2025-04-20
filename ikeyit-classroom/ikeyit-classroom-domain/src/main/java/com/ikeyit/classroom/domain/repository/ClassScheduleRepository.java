package com.ikeyit.classroom.domain.repository;

import com.ikeyit.classroom.domain.model.ClassSchedule;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.List;

/**
 * Repository interface for ClassSchedule aggregate
 */
public interface ClassScheduleRepository extends CrudRepository<ClassSchedule, Long> {
    
    /**
     * Find schedules by course ID
     * @param courseId the course ID
     * @return list of schedules for the course
     */
    List<ClassSchedule> findByCourseId(Long courseId);
    
    /**
     * Find schedules by classroom
     * @param classroom the classroom name
     * @return list of schedules for the classroom
     */
    List<ClassSchedule> findByClassroom(String classroom);
    
    /**
     * Find schedules by day of week
     * @param dayOfWeek the day of week (1-7)
     * @return list of schedules for the day
     */
    List<ClassSchedule> findByDayOfWeek(Integer dayOfWeek);
}