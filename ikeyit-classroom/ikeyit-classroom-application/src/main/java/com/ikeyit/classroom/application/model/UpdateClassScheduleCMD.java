package com.ikeyit.classroom.application.model;

import java.time.Instant;

public class UpdateClassScheduleCMD {
    private Long operatorId;
    private Long classScheduleId;
    private String classroom;
    private Instant startTime;
    private Instant endTime;

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getClassScheduleId() {
        return classScheduleId;
    }

    public void setClassScheduleId(Long classScheduleId) {
        this.classScheduleId = classScheduleId;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
}
