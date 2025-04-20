package com.ikeyit.classroom.interfaces.api.controller;

import com.ikeyit.classroom.application.model.CreateStudentCMD;
import com.ikeyit.classroom.application.model.StudentDTO;
import com.ikeyit.classroom.application.model.UpdateStudentCMD;
import com.ikeyit.classroom.application.service.StudentService;
import com.ikeyit.security.resource.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Student Controller
 */
@RestController
public class ClassroomStudentController {
    private final StudentService studentService;
    public ClassroomStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Register student information
     */
    @PostMapping("/students/me")
    public StudentDTO registerStudent(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                      @RequestBody CreateStudentCMD request) {
        request.setUserId(authenticatedUser.getId());
        return studentService.registerStudent(request);
    }

    /**
     * Update student information
     */
    @PutMapping("/students/me")
    public StudentDTO updateStudent(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                    @RequestBody UpdateStudentCMD request) {
        request.setOperatorId(authenticatedUser.getId());
        return studentService.updateStudent(request);
    }

    /**
     * Get student information
     */
    @GetMapping("/students/me")
    public StudentDTO getStudentInfo(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return studentService.getStudentByUserId(authenticatedUser.getId());
    }

    @GetMapping("/students/{userId}")
    public StudentDTO getStudentByUserId(@PathVariable Long userId) {
        return studentService.getStudentByUserId(userId);
    }
}