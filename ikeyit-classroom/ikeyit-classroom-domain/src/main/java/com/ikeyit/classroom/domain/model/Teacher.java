package com.ikeyit.classroom.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Teacher domain model representing a teacher in the system. It's an aggregate root.
 */
public class Teacher extends BaseAggregateRoot<Long> {
    private Long id;
    private String name;
    private String faculty;   // Faculty
    private String title;     // Title/Position
    private Long userId;      // Associated user ID
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * This construct is used for the persistence
     * DO NOT use it in your business logic
     */
    @ForRepo
    protected Teacher() {
    }

    public Teacher(String name, String faculty, String title, Long userId) {
        BizAssert.notEmpty(name, "Teacher name cannot be empty");
        BizAssert.notEmpty(faculty, "Faculty cannot be empty");
        BizAssert.notEmpty(title, "Title cannot be empty");
        BizAssert.notNull(userId, "User ID cannot be null");
        
        this.name = name;
        this.faculty = faculty;
        this.title = title;
        this.userId = userId;
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        this.updatedAt = createdAt;
    }

    /**
     * Update teacher information
     */
    public void update(String name, String faculty, String title) {
        BizAssert.notEmpty(name, "Teacher name cannot be empty");
        BizAssert.notEmpty(faculty, "Faculty cannot be empty");
        BizAssert.notEmpty(title, "Title cannot be empty");
        
        this.name = name;
        this.faculty = faculty;
        this.title = title;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    /**
     * Create a new course
     */
    public Course createCourse(String name, String description, String imageUrl, Integer credits,
                               Instant startDate, Instant endDate) {
        return new Course(name, description, imageUrl, credits, this.id, startDate, endDate);
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

    public String getFaculty() {
        return faculty;
    }

    public String getTitle() {
        return title;
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