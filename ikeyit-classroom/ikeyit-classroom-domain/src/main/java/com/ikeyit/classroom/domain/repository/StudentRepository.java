package com.ikeyit.classroom.domain.repository;

import com.ikeyit.classroom.domain.model.Student;
import com.ikeyit.common.data.domain.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for Student aggregate
 */
public interface StudentRepository extends CrudRepository<Student, Long> {
    
    /**
     * Find student by user ID
     * @param userId the user ID
     * @return optional student
     */
    Optional<Student> findByUserId(Long userId);
    
    /**
     * Find student by student number (学号)
     * @param studentNo the student number
     * @return optional student
     */
    Optional<Student> findByStudentNo(String studentNo);
}