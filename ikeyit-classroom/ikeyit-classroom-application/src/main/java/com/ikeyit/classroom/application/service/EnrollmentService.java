package com.ikeyit.classroom.application.service;

import com.ikeyit.classroom.application.assembler.ClassroomAssembler;
import com.ikeyit.classroom.application.model.*;
import com.ikeyit.classroom.domain.model.Course;
import com.ikeyit.classroom.domain.model.Enrollment;
import com.ikeyit.classroom.domain.model.EnrollmentStatus;
import com.ikeyit.classroom.domain.model.Student;
import com.ikeyit.classroom.domain.repository.CourseRepository;
import com.ikeyit.classroom.domain.repository.EnrollmentRepository;
import com.ikeyit.classroom.domain.repository.StudentRepository;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Enrollment Application Service
 * Responsible for coordinating domain objects to complete enrollment-related use cases
 */
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ClassroomAssembler classroomAssembler;
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             ClassroomAssembler classroomAssembler1) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.classroomAssembler = classroomAssembler1;
    }

    /**
     * Student course enrollment
     *
     * @param enrollCourseCMD@return Enrollment record ID
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public EnrollmentDTO enrollCourse(EnrollCourseCMD enrollCourseCMD) {
        // Check if student exists
        Student student = studentRepository.findByUserId(enrollCourseCMD.getStudentUserId())
            .orElseThrow(BizAssert.failSupplier("Student is not found"));
        
        // Check if course exists
        Course course = courseRepository.findById(enrollCourseCMD.getCourseId())
            .orElseThrow(BizAssert.failSupplier("Course is not found"));
        
        // Check if already enrolled in this course
        enrollmentRepository.findByStudentUserIdAndCourseId(enrollCourseCMD.getStudentUserId(), enrollCourseCMD.getCourseId())
                .ifPresent(BizAssert.failAction("Already enrolled in this course"));
        
        // Create enrollment record
        Enrollment enrollment = student.enrollCourse(course.getId());
        // Save enrollment record
        enrollmentRepository.create(enrollment);
        return classroomAssembler.toEnrollmentDTO(enrollment);
    }

    public EnrollmentDTO getStudentEnrollmentInCourse(Long studentUserId, Long courseId) {
        var enrollment = enrollmentRepository.findByStudentUserIdAndCourseId(studentUserId, courseId)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"No enrollment record found"));
        return classroomAssembler.toEnrollmentDTO(enrollment);
    }
    /**
     * Approve enrollment
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public EnrollmentDTO approveEnrollment(ApproveEnrollmentCMD cmd) {
        // Get enrollment record
        Enrollment enrollment = enrollmentRepository.findById(cmd.getEnrollmentId())
            .orElseThrow(BizAssert.failSupplier("Enrollment is not found"));
        Course course = courseRepository.findById(enrollment.getCourseId())
            .orElseThrow(BizAssert.failSupplier("Course is not found"));
        BizAssert.equals(course.getTeacherUserId(), cmd.getOperatorId(), "Only the teacher can approve enrollment");
        // Approve enrollment
        enrollment.approve();
        // Save enrollment record
        enrollmentRepository.update(enrollment);
        return classroomAssembler.toEnrollmentDTO(enrollment);
    }

    /**
     * Reject enrollment
     *
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public EnrollmentDTO rejectEnrollment(RejectEnrollmentCMD cmd) {
        // Get enrollment record
        Enrollment enrollment = enrollmentRepository.findById(cmd.getEnrollmentId())
            .orElseThrow(BizAssert.failSupplier("Enrollment is not found"));
        Course course = courseRepository.findById(enrollment.getCourseId())
            .orElseThrow(BizAssert.failSupplier("Course is not found"));
        BizAssert.equals(course.getTeacherUserId(), cmd.getOperatorId(), "Only the teacher can reject enrollment");
        // Reject enrollment
        enrollment.reject();
        // Save enrollment record
        enrollmentRepository.update(enrollment);
        return classroomAssembler.toEnrollmentDTO(enrollment);
    }
    
    /**
     * Cancel enrollment
     *
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public EnrollmentDTO cancelEnrollment(CancelEnrollmentCMD cmd) {
        // Get enrollment record
        Enrollment enrollment = enrollmentRepository.findById(cmd.getEnrollmentId())
            .orElseThrow(BizAssert.failSupplier("Enrollment is not found"));
        BizAssert.equals(enrollment.getStudentUserId(), cmd.getOperatorId(), "Only the student can cancel enrollment");
        // Cancel enrollment
        enrollment.cancel();
        
        // Save enrollment record
        enrollmentRepository.update(enrollment);
        return classroomAssembler.toEnrollmentDTO(enrollment);
    }

    /**
     * Get student's enrollment records
     *
     */
    public List<EnrollmentDTO> getEnrollmentsByStudentUserId(Long studentUserId) {
        return enrollmentRepository.findByStudentUserId(studentUserId).stream()
            .map(classroomAssembler::toEnrollmentDTO)
            .toList();
    }

    /**
     * Get course enrollment records
     *
     * @param courseId Course ID
     * @return List of enrollment records
     */
    public List<EnrollmentDTO> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
            .map(classroomAssembler::toEnrollmentDTO)
            .toList();
    }

    /**
     * Get enrollment records by status
     *
     * @param status Enrollment status
     * @return List of enrollment records
     */
    public List<EnrollmentDTO> getEnrollmentsByStatus(EnrollmentStatus status) {
        return enrollmentRepository.findByStatus(status).stream()
            .map(classroomAssembler::toEnrollmentDTO)
            .toList();
    }

    /**
     * Get enrollment record
     *
     * @param enrollmentId Enrollment record ID
     * @return Enrollment record
     */
    public EnrollmentDTO getEnrollment(Long enrollmentId) {
        var enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"Enrollment is not found"));
        return classroomAssembler.toEnrollmentDTO(enrollment);
    }
}