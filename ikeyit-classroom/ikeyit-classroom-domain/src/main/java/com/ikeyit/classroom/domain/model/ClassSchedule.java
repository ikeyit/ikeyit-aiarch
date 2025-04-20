package com.ikeyit.classroom.domain.model;

import com.ikeyit.classroom.domain.event.ClassScheduleCreatedEvent;
import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * ClassSchedule domain model representing a course schedule in the system. It's an aggregate root.
 */
public class ClassSchedule extends BaseAggregateRoot<Long> {
    private Long id;
    private Long courseId;
    private String classroom;
    private Instant startTime;
    private Instant endTime;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * This construct is used for the persistence
     * DO NOT use it in your business logic
     */
    @ForRepo
    protected ClassSchedule() {
    }

    public ClassSchedule(Long courseId, String classroom, Instant startTime, Instant endTime) {
        BizAssert.notNull(courseId, "Course ID cannot be null");
        BizAssert.notEmpty(classroom, "Classroom cannot be empty");
        BizAssert.notNull(startTime, "Start time cannot be null");
        BizAssert.notNull(endTime, "End time cannot be null");
        BizAssert.isTrue(startTime.isBefore(endTime), "Start time must be before end time");
        
        this.courseId = courseId;
        this.classroom = classroom;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        this.updatedAt = createdAt;
        
        // Generate a domain event to notify the domain model is created
        addDomainEvent(() -> new ClassScheduleCreatedEvent(this));
    }

    /**
     * Reschedule the class
     */
    public void reschedule(String classroom, Instant startTime, Instant endTime) {
        BizAssert.notEmpty(classroom, "Classroom cannot be empty");
        BizAssert.notNull(startTime, "Start time cannot be null");
        BizAssert.notNull(endTime, "End time cannot be null");
        BizAssert.isTrue(startTime.isBefore(endTime), "Start time must be before end time");
        
        this.classroom = classroom;
        this.startTime = startTime;
        this.endTime = endTime;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }


    public void delete() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    @Override
    public void assignId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getClassroom() {
        return classroom;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}