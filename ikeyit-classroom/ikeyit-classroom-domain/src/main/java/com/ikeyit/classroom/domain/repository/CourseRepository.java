package com.ikeyit.classroom.domain.repository;

import com.ikeyit.classroom.domain.model.Course;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.List;

/**
 * Repository interface for Course aggregate
 */
public interface CourseRepository extends CrudRepository<Course, Long> {
    
    /**
     * Find all courses
     * @return list of courses
     */
    List<Course> findAll();
    
    /**
     * Find courses by teacher user ID
     * @param teacherUserId the teacher user ID
     * @return list of courses taught by the teacher
     */
    List<Course> findByTeacherUserId(Long teacherUserId);
}