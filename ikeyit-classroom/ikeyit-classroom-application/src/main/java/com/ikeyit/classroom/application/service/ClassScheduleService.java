package com.ikeyit.classroom.application.service;

import com.ikeyit.classroom.application.assembler.ClassroomAssembler;
import com.ikeyit.classroom.application.model.ClassScheduleDTO;
import com.ikeyit.classroom.application.model.CreateClassScheduleCMD;
import com.ikeyit.classroom.application.model.DeleteClassScheduleCMD;
import com.ikeyit.classroom.application.model.UpdateClassScheduleCMD;
import com.ikeyit.classroom.domain.model.ClassSchedule;
import com.ikeyit.classroom.domain.model.Course;
import com.ikeyit.classroom.domain.repository.ClassScheduleRepository;
import com.ikeyit.classroom.domain.repository.CourseRepository;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class Schedule Application Service
 * Responsible for coordinating domain objects to complete class schedule related use cases
 */
@Service
public class ClassScheduleService {

    private final ClassScheduleRepository classScheduleRepository;
    private final CourseRepository courseRepository;
    private final ClassroomAssembler classroomAssembler;
    public ClassScheduleService(ClassScheduleRepository classScheduleRepository,
                                CourseRepository courseRepository, ClassroomAssembler classroomAssembler) {
        this.classScheduleRepository = classScheduleRepository;
        this.courseRepository = courseRepository;
        this.classroomAssembler = classroomAssembler;
    }

    /**
     * Create class schedule
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public ClassScheduleDTO createClassSchedule(CreateClassScheduleCMD cmd) {
        // Check if course exists
        Course course = courseRepository.findById(cmd.getCourseId())
            .orElseThrow(BizAssert.failSupplier("Course is not found"));
        BizAssert.equals(course.getTeacherUserId(), cmd.getOperatorId(), "Only the teacher of the course can create class schedule");
        // Create class schedule
        ClassSchedule classSchedule = new ClassSchedule(
            cmd.getCourseId(), 
            cmd.getClassroom(),
            cmd.getStartTime(), 
            cmd.getEndTime());
        
        // Save class schedule
        classScheduleRepository.update(classSchedule);
        return classroomAssembler.toClassScheduleDTO(classSchedule);
    }

    /**
     * Update class schedule
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public ClassScheduleDTO updateClassSchedule(UpdateClassScheduleCMD cmd) {
        // Get class schedule
        ClassSchedule classSchedule = classScheduleRepository.findById(cmd.getClassScheduleId())
            .orElseThrow(BizAssert.failSupplier("ClassSchedule is not found"));
        Course course = courseRepository.findById(classSchedule.getCourseId())
            .orElseThrow(BizAssert.failSupplier("Course is not found"));
        BizAssert.equals(course.getTeacherUserId(), cmd.getOperatorId(), "Only the teacher of the course can create class schedule");
        // Update class schedule
        classSchedule.reschedule(cmd.getClassroom(), cmd.getStartTime(), cmd.getEndTime());
        
        // Save class schedule
        classScheduleRepository.update(classSchedule);
        return classroomAssembler.toClassScheduleDTO(classSchedule);
    }

    /**
     * Get all class schedules for a course
     *
     * @param courseId Course ID
     * @return List of class schedules
     */
    public List<ClassScheduleDTO> getClassSchedulesByCourse(Long courseId) {
        return classScheduleRepository.findByCourseId(courseId).stream()
            .map(classroomAssembler::toClassScheduleDTO)
            .toList();
    }

    /**
     * Get class schedule
     *
     * @param scheduleId Class schedule ID
     * @return Class schedule
     */
    public ClassScheduleDTO getClassSchedule(Long scheduleId) {
        return classScheduleRepository.findById(scheduleId)
            .map(classroomAssembler::toClassScheduleDTO)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"ClassSchedule is not found"));
    }

    /**
     * Delete class schedule
     *
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public void deleteClassSchedule(DeleteClassScheduleCMD cmd) {
        ClassSchedule classSchedule = classScheduleRepository.findById(cmd.getClassScheduleId())
            .orElseThrow(BizAssert.failSupplier("ClassSchedule is not found"));
        Course course = courseRepository.findById(classSchedule.getCourseId())
            .orElseThrow(BizAssert.failSupplier("Course is not found"));
        BizAssert.equals(course.getTeacherUserId(), cmd.getOperatorId(), "Only the teacher of the course can delete class schedule");
        classSchedule.delete();
        classScheduleRepository.delete(classSchedule);
    }
}