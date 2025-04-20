package com.ikeyit.classroom.domain.repository;

import com.ikeyit.classroom.domain.model.Enrollment;
import com.ikeyit.classroom.domain.model.EnrollmentStatus;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Enrollment aggregate
 */
public interface EnrollmentRepository extends CrudRepository<Enrollment, Long> {
    
    /**
     * Find enrollments by student user ID
     * @param studentUserId the student user ID
     * @return list of enrollments for the student
     */
    List<Enrollment> findByStudentUserId(Long studentUserId);
    
    /**
     * Find enrollments by course ID
     * @param courseId the course ID
     * @return list of enrollments for the course
     */
    List<Enrollment> findByCourseId(Long courseId);
    
    /**
     * Find enrollments by status
     * @param status the enrollment status
     * @return list of enrollments with the specified status
     */
    List<Enrollment> findByStatus(EnrollmentStatus status);
    
    /**
     * Find enrollment by student ID and course ID
     * @param studentUserId the student user ID
     * @param courseId the course ID
     * @return optional enrollment
     */
    Optional<Enrollment> findByStudentUserIdAndCourseId(Long studentUserId, Long courseId);
}