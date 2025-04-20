package com.ikeyit.classroom.domain.repository;

import com.ikeyit.classroom.domain.model.Teacher;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for Teacher aggregate
 */
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    
    /**
     * Find teacher by user ID
     * @param userId the user ID
     * @return optional teacher
     */
    Optional<Teacher> findByUserId(Long userId);
}