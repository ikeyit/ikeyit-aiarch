package com.ikeyit.classroom.interfaces.api.controller;

import com.ikeyit.classroom.application.model.ClassScheduleDTO;
import com.ikeyit.classroom.application.model.CreateClassScheduleCMD;
import com.ikeyit.classroom.application.model.DeleteClassScheduleCMD;
import com.ikeyit.classroom.application.model.UpdateClassScheduleCMD;
import com.ikeyit.classroom.application.service.ClassScheduleService;
import com.ikeyit.security.resource.AuthenticatedUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class Schedule Controller
 */
@RestController
public class ClassroomClassScheduleController {
    private final ClassScheduleService classScheduleService;

    public ClassroomClassScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    /**
     * Create class schedule
     */
    @PostMapping("/schedules")
    @PreAuthorize("hasRole('teacher')")
    public ClassScheduleDTO createClassSchedule(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                @RequestBody CreateClassScheduleCMD request) {
        request.setOperatorId(authenticatedUser.getId());
        return classScheduleService.createClassSchedule(request);
    }

    /**
     * Update class schedule
     */
    @PutMapping("/schedules/{scheduleId}")
    @PreAuthorize("hasRole('teacher')")
    public ClassScheduleDTO updateClassSchedule(@PathVariable Long scheduleId,
                                                @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                @RequestBody UpdateClassScheduleCMD request) {
        request.setOperatorId(authenticatedUser.getId());
        request.setClassScheduleId(scheduleId);
        return classScheduleService.updateClassSchedule(request);
    }

    /**
     * Get all class schedules for a course
     */
    @GetMapping("/courses/{courseId}/schedules")
    public List<ClassScheduleDTO> getClassSchedulesByCourse(@PathVariable Long courseId) {
        return classScheduleService.getClassSchedulesByCourse(courseId);
    }

    /**
     * Get class schedule details
     */
    @GetMapping("/schedules/{scheduleId}")
    public ClassScheduleDTO getClassSchedule(@PathVariable Long scheduleId) {
       return classScheduleService.getClassSchedule(scheduleId);
    }

    /**
     * Delete class schedule
     */
    @DeleteMapping("/schedules/{scheduleId}")
    @PreAuthorize("hasRole('teacher')")
    public void deleteClassSchedule(@PathVariable Long scheduleId,
                                    @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var request = new DeleteClassScheduleCMD();
        request.setClassScheduleId(scheduleId);
        request.setOperatorId(authenticatedUser.getId());
        classScheduleService.deleteClassSchedule(request);
    }
}