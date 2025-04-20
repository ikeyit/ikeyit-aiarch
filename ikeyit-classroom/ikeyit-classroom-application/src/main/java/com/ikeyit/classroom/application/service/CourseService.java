package com.ikeyit.classroom.application.service;

import com.ikeyit.classroom.application.assembler.ClassroomAssembler;
import com.ikeyit.classroom.application.model.*;
import com.ikeyit.classroom.domain.model.Course;
import com.ikeyit.classroom.domain.repository.CourseRepository;
import com.ikeyit.classroom.domain.repository.TeacherRepository;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Course Application Service
 * Responsible for coordinating domain objects to complete course-related use cases
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomAssembler classroomAssembler;

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository, ClassroomAssembler classroomAssembler) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.classroomAssembler = classroomAssembler;
    }

    /**
     * Create course
     *
     * @param createCourseCMD@return Course ID
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public CourseDTO createCourse(CreateCourseCMD createCourseCMD) {
        BizAssert.notNull(createCourseCMD, "createCourseCMD is null");
        // Create course domain object
        Course course = new Course(createCourseCMD.getName(),
            createCourseCMD.getDescription(),
            createCourseCMD.getImageUrl(),
            createCourseCMD.getCredits(),
            createCourseCMD.getTeacherUserId(),
            createCourseCMD.getStartDate(),
            createCourseCMD.getEndDate());
        
        // Save course
        courseRepository.create(course);
        return toCourseDTO(course);
    }

    /**
     * Update course
     *
     * @param updateCourseCMD
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public CourseDTO updateCourse(UpdateCourseCMD updateCourseCMD) {
        BizAssert.notNull(updateCourseCMD, "updateCourseCMD is null");
        // Get course
        Course course = courseRepository.findById(updateCourseCMD.getCourseId())
                .orElseThrow(BizAssert.failSupplier("Course does not exist"));
        BizAssert.equals(course.getTeacherUserId(), updateCourseCMD.getOperatorId(), "Only the teacher can update course");
        // Update course
        course.update(updateCourseCMD.getName(),
            updateCourseCMD.getDescription(),
            updateCourseCMD.getImageUrl(),
            updateCourseCMD.getCredits(),
            updateCourseCMD.getStartDate(),
            updateCourseCMD.getEndDate());
        
        // Save course
        courseRepository.update(course);
        return toCourseDTO(course);
    }

    /**
     * Publish course
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public CourseDTO publishCourse(PublishCourseCMD cmd) {
        // Get course
        Course course = courseRepository.findById(cmd.getCourseId())
                .orElseThrow(BizAssert.failSupplier("Course does not exist"));
        BizAssert.equals(course.getTeacherUserId(), cmd.getOperatorId(),"Only the teacher can publish course");
        // Publish course
        course.publish();
        
        // Save course
        courseRepository.update(course);
        return toCourseDTO(course);
    }

    /**
     * Cancel course
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public CourseDTO cancelCourse(CancelCourseCMD cmd) {
        // Get course
        Course course = courseRepository.findById(cmd.getCourseId())
                .orElseThrow(BizAssert.failSupplier("Course does not exist"));
        BizAssert.equals(course.getTeacherUserId(), cmd.getOperatorId(),"Only the teacher can publish course");
        // Cancel course
        course.cancel();
        
        // Save course
        courseRepository.update(course);
        return toCourseDTO(course);
    }

    /**
     * Get course
     *
     * @param courseId Course ID
     * @return Course
     */
    public CourseDTO getCourse(Long courseId) {
        var course = courseRepository.findById(courseId)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"Course does not exist"));
        return toCourseDTO(course);
    }

    /**
     * Get all courses
     *
     * @return Course list
     */
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
            .map(this::toCourseDTO)
            .toList();
    }

    /**
     * Get courses by teacher
     *
     */
    public List<CourseDTO> getCoursesByTeacherUserId(Long teacherUserId) {
        var teacher = teacherRepository.findByUserId(teacherUserId)
            .orElseThrow(BizAssert.failSupplier("Teacher does not exist"));
        return courseRepository.findByTeacherUserId(teacherUserId).stream()
            .map(course -> classroomAssembler.toCourseDTO(course, teacher))
            .toList();
    }

    private CourseDTO toCourseDTO(Course course) {
        var teacher = teacherRepository.findByUserId(course.getTeacherUserId())
            .orElseThrow(BizAssert.failSupplier("Teacher does not exist"));
        return classroomAssembler.toCourseDTO(course, teacher);
    }
}