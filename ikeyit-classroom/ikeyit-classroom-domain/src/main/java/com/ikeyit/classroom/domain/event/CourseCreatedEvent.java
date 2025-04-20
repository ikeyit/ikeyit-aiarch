package com.ikeyit.classroom.domain.event;

import com.ikeyit.classroom.domain.model.Course;
import com.ikeyit.common.data.domain.BaseDomainEvent;

/**
 * Event triggered when a new course is created
 */
public class CourseCreatedEvent extends BaseDomainEvent {
    private Long courseId;
    private String courseName;
    private Long teacherId;

    protected CourseCreatedEvent() {
    }

    public CourseCreatedEvent(Course course) {
        this.courseId = course.getId();
        this.courseName = course.getName();
        this.teacherId = course.getTeacherUserId();
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public Long getTeacherId() {
        return teacherId;
    }
}