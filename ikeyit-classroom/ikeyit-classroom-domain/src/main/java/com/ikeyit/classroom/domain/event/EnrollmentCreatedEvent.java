package com.ikeyit.classroom.domain.event;

import com.ikeyit.classroom.domain.model.Enrollment;
import com.ikeyit.common.data.domain.BaseDomainEvent;
import com.ikeyit.common.data.domain.ForRepo;

/**
 * Event triggered when a new enrollment is created
 */
public class EnrollmentCreatedEvent extends BaseDomainEvent {
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;

    @ForRepo
    protected EnrollmentCreatedEvent() {
    }

    public EnrollmentCreatedEvent(Enrollment enrollment) {
        this.enrollmentId = enrollment.getId();
        this.studentId = enrollment.getStudentUserId();
        this.courseId = enrollment.getCourseId();
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }
}