package com.ikeyit.classroom.application.model;

public class CreateTeacherCMD {
    private String name;
    private String faculty;
    private String title;
    private Long userId;

    public CreateTeacherCMD() {
    }

    public CreateTeacherCMD(String name, String faculty, String title, Long userId) {
        this.name = name;
        this.faculty = faculty;
        this.title = title;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}