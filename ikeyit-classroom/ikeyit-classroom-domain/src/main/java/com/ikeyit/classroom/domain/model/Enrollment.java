package com.ikeyit.classroom.domain.model;

import com.ikeyit.classroom.domain.event.EnrollmentApprovedEvent;
import com.ikeyit.classroom.domain.event.EnrollmentCreatedEvent;
import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Enrollment domain model representing a course enrollment in the system. It's an aggregate root.
 */
public class Enrollment extends BaseAggregateRoot<Long> {
    private Long id;
    private Long studentUserId;
    private Long courseId;
    private EnrollmentStatus status;
    private Instant enrollDate;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * This construct is used for the persistence
     * DO NOT use it in your business logic
     */
    @ForRepo
    protected Enrollment() {
    }

    public Enrollment(Long studentUserId, Long courseId) {
        BizAssert.notNull(studentUserId, "Student ID cannot be null");
        BizAssert.notNull(courseId, "Course ID cannot be null");
        
        this.studentUserId = studentUserId;
        this.courseId = courseId;
        this.status = EnrollmentStatus.PENDING;
        this.enrollDate = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        this.createdAt = enrollDate;
        this.updatedAt = enrollDate;
        
        // Generate a domain event to notify the domain model is created
        addDomainEvent(() -> new EnrollmentCreatedEvent(this));
    }

    /**
     * Approve the enrollment
     */
    public void approve() {
        BizAssert.isTrue(status == EnrollmentStatus.PENDING, "Only pending enrollments can be approved");
        
        this.status = EnrollmentStatus.APPROVED;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        
        addDomainEvent(() -> new EnrollmentApprovedEvent(this));
    }

    /**
     * Reject the enrollment
     */
    public void reject() {
        BizAssert.isTrue(status == EnrollmentStatus.PENDING, "Only pending enrollments can be rejected");
        
        this.status = EnrollmentStatus.REJECTED;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    /**
     * Cancel the enrollment
     * Students can cancel pending or approved enrollments
     */
    public void cancel() {
        BizAssert.isTrue(status == EnrollmentStatus.PENDING || status == EnrollmentStatus.APPROVED, 
                "Only pending or approved enrollments can be cancelled");
        
        this.status = EnrollmentStatus.CANCELLED;
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

    public Long getStudentUserId() {
        return studentUserId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public Instant getEnrollDate() {
        return enrollDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}