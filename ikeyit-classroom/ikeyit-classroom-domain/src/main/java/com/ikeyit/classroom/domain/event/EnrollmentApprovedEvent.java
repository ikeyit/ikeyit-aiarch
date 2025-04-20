package com.ikeyit.classroom.domain.event;

import com.ikeyit.classroom.domain.model.Enrollment;
import com.ikeyit.common.data.domain.BaseDomainEvent;

/**
 * Event triggered when an enrollment is approved
 */
public class EnrollmentApprovedEvent extends BaseDomainEvent {
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;

    protected EnrollmentApprovedEvent() {
    }

    public EnrollmentApprovedEvent(Enrollment enrollment) {
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