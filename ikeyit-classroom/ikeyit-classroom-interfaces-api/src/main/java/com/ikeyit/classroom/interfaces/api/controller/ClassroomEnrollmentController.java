package com.ikeyit.classroom.interfaces.api.controller;

import com.ikeyit.classroom.application.model.*;
import com.ikeyit.classroom.application.service.EnrollmentService;
import com.ikeyit.security.resource.AuthenticatedUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Enrollment Controller
 */
@RestController
public class ClassroomEnrollmentController {
    private final EnrollmentService enrollmentService;

    public ClassroomEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * Student course enrollment
     */
    @PostMapping("/enrollments")
    public EnrollmentDTO enrollCourse(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                      @RequestBody EnrollCourseCMD request) {
        request.setStudentUserId(authenticatedUser.getId());
        return enrollmentService.enrollCourse(request);
    }

    /**
     * Approve enrollment
     */
    @PostMapping("/enrollments/{enrollmentId}/approve")
    @PreAuthorize("hasRole('teacher')")
    public EnrollmentDTO approveEnrollment(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                           @PathVariable Long enrollmentId) {
        var approveEnrollment = new ApproveEnrollmentCMD();
        approveEnrollment.setEnrollmentId(enrollmentId);
        approveEnrollment.setOperatorId(authenticatedUser.getId());
        return enrollmentService.approveEnrollment(approveEnrollment);
    }

    /**
     * Reject enrollment
     */
    @PostMapping("/enrollments/{enrollmentId}/reject")
    @PreAuthorize("hasRole('teacher')")
    public EnrollmentDTO rejectEnrollment(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                          @PathVariable Long enrollmentId) {
        var rejectEnrollment = new RejectEnrollmentCMD();
        rejectEnrollment.setEnrollmentId(enrollmentId);
        rejectEnrollment.setOperatorId(authenticatedUser.getId());
        return enrollmentService.rejectEnrollment(rejectEnrollment);
    }

    /**
     * Cancel enrollment
     */
    @PostMapping("/enrollments/{enrollmentId}/cancel")
    public EnrollmentDTO cancelEnrollment(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                          @PathVariable Long enrollmentId) {
        var cancelEnrollment = new CancelEnrollmentCMD();
        cancelEnrollment.setEnrollmentId(enrollmentId);
        cancelEnrollment.setOperatorId(authenticatedUser.getId());
        return enrollmentService.cancelEnrollment(cancelEnrollment);
    }

    /**
     * Get enrollment details
     */
    @GetMapping("/enrollments/{enrollmentId}")
    public EnrollmentDTO getEnrollment(@PathVariable Long enrollmentId) {
        return enrollmentService.getEnrollment(enrollmentId);
    }

    /**
     * Get all enrollments for current student
     */
    @GetMapping("/students/me/enrollments")
    public List<EnrollmentDTO> getMyEnrollments(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return enrollmentService.getEnrollmentsByStudentUserId(authenticatedUser.getId());
    }

    /**
     * Get student's enrollment in a specific course
     */
    @GetMapping("/students/me/courses/{courseId}/enrollment")
    public EnrollmentDTO getStudentEnrollmentInCourse(
        @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
        @PathVariable Long courseId
    ) {
        return enrollmentService.getStudentEnrollmentInCourse(authenticatedUser.getId(), courseId);
    }

    /**
     * Get all enrollments for a student
     */
    @GetMapping("/students/{studentUserId}/enrollments")
    public List<EnrollmentDTO> getEnrollmentsByStudentUserId(@PathVariable Long studentUserId) {
        return enrollmentService.getEnrollmentsByStudentUserId(studentUserId);
    }

    /**
     * Get all enrollments for a course
     */
    @GetMapping("/courses/{courseId}/enrollments")
    public List<EnrollmentDTO> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }
}