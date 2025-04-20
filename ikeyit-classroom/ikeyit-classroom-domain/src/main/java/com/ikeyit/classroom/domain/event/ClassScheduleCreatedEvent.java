package com.ikeyit.classroom.domain.event;

import com.ikeyit.classroom.domain.model.ClassSchedule;
import com.ikeyit.common.data.domain.BaseDomainEvent;

/**
 * Event triggered when a new class schedule is created
 */
public class ClassScheduleCreatedEvent extends BaseDomainEvent {
    private Long scheduleId;
    private Long courseId;
    private String classroom;

    protected ClassScheduleCreatedEvent() {
    }

    public ClassScheduleCreatedEvent(ClassSchedule classSchedule) {
        this.scheduleId = classSchedule.getId();
        this.courseId = classSchedule.getCourseId();
        this.classroom = classSchedule.getClassroom();
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getClassroom() {
        return classroom;
    }
}