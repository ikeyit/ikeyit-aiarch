package com.ikeyit.classroom.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Student domain model representing a student in the system. It's an aggregate root.
 */
public class Student extends BaseAggregateRoot<Long> {
    private Long id;
    private String name;
    private String studentNo; // Student ID
    private String faculty;   // Faculty
    private String className; // Class name
    private Long userId;      // Associated user ID
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * This construct is used for the persistence
     * DO NOT use it in your business logic
     */
    @ForRepo
    protected Student() {
    }

    public Student(String name, String studentNo, String faculty, String className, Long userId) {
        BizAssert.notEmpty(name, "Student name cannot be empty");
        BizAssert.notEmpty(studentNo, "Student ID cannot be empty");
        BizAssert.notEmpty(faculty, "Faculty cannot be empty");
        BizAssert.notEmpty(className, "Class name cannot be empty");
        BizAssert.notNull(userId, "User ID cannot be null");
        
        this.name = name;
        this.studentNo = studentNo;
        this.faculty = faculty;
        this.className = className;
        this.userId = userId;
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        this.updatedAt = createdAt;
    }

    /**
     * Update student information
     */
    public void update(String name, String faculty, String className) {
        BizAssert.notEmpty(name, "Student name cannot be empty");
        BizAssert.notEmpty(faculty, "Faculty cannot be empty");
        BizAssert.notEmpty(className, "Class name cannot be empty");
        
        this.name = name;
        this.faculty = faculty;
        this.className = className;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    /**
     * Enroll in a course
     */
    public Enrollment enrollCourse(Long courseId) {
        BizAssert.notNull(courseId, "Course ID cannot be null");
        
        return new Enrollment(this.id, courseId);
    }

    // Getters
    public Long getId() {
        return id;
    }

    @Override
    public void assignId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getClassName() {
        return className;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}