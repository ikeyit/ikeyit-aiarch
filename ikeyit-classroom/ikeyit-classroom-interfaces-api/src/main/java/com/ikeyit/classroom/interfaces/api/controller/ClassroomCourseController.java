package com.ikeyit.classroom.interfaces.api.controller;

import com.ikeyit.classroom.application.model.*;
import com.ikeyit.classroom.application.service.CourseService;
import com.ikeyit.security.resource.AuthenticatedUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course Controller
 */
@RestController
public class ClassroomCourseController {
    private final CourseService courseService;
    public ClassroomCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Create course
     */
    @PostMapping("/courses")
    @PreAuthorize("hasRole('teacher')")
    public CourseDTO createCourse(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                  @RequestBody CreateCourseCMD request) {
        request.setTeacherUserId(authenticatedUser.getId());
        return courseService.createCourse(request);
    }

    /**
     * Update course
     */
    @PutMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('teacher')")
    public CourseDTO updateCourse(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                  @PathVariable Long courseId,
                                  @RequestBody UpdateCourseCMD request) {
        request.setOperatorId(authenticatedUser.getId());
        request.setCourseId(courseId);
        return courseService.updateCourse(request);
    }

    /**
     * Publish course
     */
    @PostMapping("/courses/{courseId}/publish")
    @PreAuthorize("hasRole('teacher')")
    public CourseDTO publishCourse(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                   @PathVariable Long courseId,
                                   @RequestBody PublishCourseCMD request) {
        request.setOperatorId(authenticatedUser.getId());
        request.setCourseId(courseId);
        return courseService.publishCourse(request);
    }

    /**
     * Cancel course
     */
    @PostMapping("/courses/{courseId}/cancel")
    @PreAuthorize("hasRole('teacher')")
    public CourseDTO cancelCourse(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                  @PathVariable Long courseId,
                                  @RequestBody CancelCourseCMD request) {
        request.setOperatorId(authenticatedUser.getId());
        request.setCourseId(courseId);
        return courseService.cancelCourse(request);
    }

    /**
     * Get course details
     */
    @GetMapping("/courses/{courseId}")
    public CourseDTO getCourse(@PathVariable Long courseId) {
        return courseService.getCourse(courseId);
    }

    /**
     * Get all courses
     */
    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    /**
     * Get teacher's courses
     */
    @GetMapping("/teachers/me/courses")
    @PreAuthorize("hasRole('teacher')")
    public List<CourseDTO> getCoursesByTeacherUserId(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return courseService.getCoursesByTeacherUserId(authenticatedUser.getId());
    }

    /**
     * Get teacher's courses
     */
    @GetMapping("/teachers/{teacherUserId}/courses")
    public List<CourseDTO> getCoursesByTeacherUserId(@PathVariable Long teacherUserId) {
       return courseService.getCoursesByTeacherUserId(teacherUserId);
    }
}