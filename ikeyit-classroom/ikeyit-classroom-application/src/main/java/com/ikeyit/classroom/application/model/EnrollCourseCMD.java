package com.ikeyit.classroom.application.model;

public class EnrollCourseCMD {
    private Long studentUserId;
    private Long courseId;

    public EnrollCourseCMD() {
    }

    public EnrollCourseCMD(Long studentUserId, Long courseId) {
        this.studentUserId = studentUserId;
        this.courseId = courseId;
    }

    public Long getStudentUserId() {
        return studentUserId;
    }

    public void setStudentUserId(Long studentUserId) {
        this.studentUserId = studentUserId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}