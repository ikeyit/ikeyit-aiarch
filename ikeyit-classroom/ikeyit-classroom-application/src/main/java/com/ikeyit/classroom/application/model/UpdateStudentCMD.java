package com.ikeyit.classroom.application.model;

public class UpdateStudentCMD {
    private Long operatorId;
    private String name;
    private String faculty;
    private String className;

    public UpdateStudentCMD() {
    }

    public UpdateStudentCMD(Long operatorId, String name, String faculty, String className) {
        this.operatorId = operatorId;
        this.name = name;
        this.faculty = faculty;
        this.className = className;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}