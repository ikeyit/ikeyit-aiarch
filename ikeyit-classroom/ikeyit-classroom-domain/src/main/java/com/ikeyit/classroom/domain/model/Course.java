package com.ikeyit.classroom.domain.model;

import com.ikeyit.classroom.domain.event.CourseCreatedEvent;
import com.ikeyit.classroom.domain.event.CoursePublishedEvent;
import com.ikeyit.classroom.domain.event.CourseUpdatedEvent;
import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Course domain model representing a course in the system. It's an aggregate root.
 */
public class Course extends BaseAggregateRoot<Long> {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer credits;
    private Long teacherUserId;
    private Instant startDate;
    private Instant endDate;
    private CourseStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * This construct is used for the persistence
     * DO NOT use it in your business logic
     */
    @ForRepo
    protected Course() {
    }

    public Course(String name, String description, String imageUrl, Integer credits, Long teacherUserId,
                  Instant startDate, Instant endDate) {
        BizAssert.notEmpty(name, "Course name cannot be empty");
        BizAssert.notNull(credits, "Credits cannot be null");
        BizAssert.notNull(teacherUserId, "Teacher ID cannot be null");
        
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.credits = credits;
        this.teacherUserId = teacherUserId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CourseStatus.DRAFT;
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        this.updatedAt = createdAt;
        
        // Generate a domain event to notify the domain model is created
        addDomainEvent(() -> new CourseCreatedEvent(this));
    }

    /**
     * Update course information
     */
    public void update(String name, String description, String imageUrl, Integer credits,
                       Instant startDate, Instant endDate) {
        BizAssert.isTrue(status == CourseStatus.DRAFT, "Only courses in draft status can be modified");
        BizAssert.notEmpty(name, "Course name cannot be empty");
        BizAssert.notNull(credits, "Credits cannot be null");
        
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.credits = credits;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        
        addDomainEvent(() -> new CourseUpdatedEvent(this));
    }

    /**
     * Publish the course
     */
    public void publish() {
        BizAssert.isTrue(status == CourseStatus.DRAFT, "Only courses in draft status can be published");
        
        this.status = CourseStatus.PUBLISHED;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        
        addDomainEvent(() -> new CoursePublishedEvent(this));
    }

    /**
     * Cancel the course
     */
    public void cancel() {
        BizAssert.isTrue(status == CourseStatus.PUBLISHED, "Only published courses can be cancelled");
        this.status = CourseStatus.CANCELLED;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
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

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getCredits() {
        return credits;
    }

    public Long getTeacherUserId() {
        return teacherUserId;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}