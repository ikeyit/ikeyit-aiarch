package com.ikeyit.classroom.domain.event;

import com.ikeyit.classroom.domain.model.Course;
import com.ikeyit.common.data.domain.BaseDomainEvent;

/**
 * Event triggered when a course is published
 */
public class CoursePublishedEvent extends BaseDomainEvent {
    private Long courseId;
    private String courseName;
    private Long teacherId;

    protected CoursePublishedEvent() {
    }

    public CoursePublishedEvent(Course course) {
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