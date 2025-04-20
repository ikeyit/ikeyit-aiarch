package com.ikeyit.classroom.application.model;

public class UpdateTeacherCMD {
    private Long operatorId;
    private String name;
    private String faculty;
    private String title;

    public UpdateTeacherCMD() {
    }

    public UpdateTeacherCMD(Long operatorId, String name, String faculty, String title) {
        this.operatorId = operatorId;
        this.name = name;
        this.faculty = faculty;
        this.title = title;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}