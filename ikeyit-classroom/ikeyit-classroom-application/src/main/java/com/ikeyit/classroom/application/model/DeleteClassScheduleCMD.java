package com.ikeyit.classroom.application.model;

public class DeleteClassScheduleCMD {
    private Long operatorId;
    private Long classScheduleId;

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
}
