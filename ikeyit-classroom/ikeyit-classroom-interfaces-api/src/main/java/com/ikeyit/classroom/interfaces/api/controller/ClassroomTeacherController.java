package com.ikeyit.classroom.interfaces.api.controller;

import com.ikeyit.classroom.application.model.CreateTeacherCMD;
import com.ikeyit.classroom.application.model.TeacherDTO;
import com.ikeyit.classroom.application.model.UpdateTeacherCMD;
import com.ikeyit.classroom.application.service.TeacherService;
import com.ikeyit.security.resource.AuthenticatedUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Teacher Controller
 */
@RestController
public class ClassroomTeacherController {
    private final TeacherService teacherService;

    public ClassroomTeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * Register teacher information
     */
    @PostMapping("/teachers/me")
    @PreAuthorize("hasRole('teacher')")
    public TeacherDTO registerTeacher(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                      @RequestBody CreateTeacherCMD request) {
        request.setUserId(authenticatedUser.getId());
        return teacherService.registerTeacher(request);
    }

    /**
     * Update teacher information
     */
    @PutMapping("/teachers/me")
    @PreAuthorize("hasRole('teacher')")
    public TeacherDTO updateTeacher(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                    @RequestBody UpdateTeacherCMD request) {
        request.setOperatorId(authenticatedUser.getId());
        return teacherService.updateTeacher(request);
    }

    /**
     * Get teacher information
     */
    @GetMapping("/teachers/me")
    @PreAuthorize("hasRole('teacher')")
    public TeacherDTO getTeacherInfo(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return teacherService.getTeacherById(authenticatedUser.getId());
    }

    /**
     * Get teacher information by user ID
     */
    @GetMapping("/teachers/{teacherUserId}")
    public TeacherDTO getTeacherByUserId(@PathVariable Long teacherUserId) {
        return teacherService.getTeacherByUserId(teacherUserId);
    }

}