package com.ikeyit.classroom.application.service;

import com.ikeyit.classroom.application.assembler.ClassroomAssembler;
import com.ikeyit.classroom.application.model.CreateTeacherCMD;
import com.ikeyit.classroom.application.model.TeacherDTO;
import com.ikeyit.classroom.application.model.UpdateTeacherCMD;
import com.ikeyit.classroom.domain.model.Teacher;
import com.ikeyit.classroom.domain.repository.TeacherRepository;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Teacher Application Service
 * Responsible for coordinating domain objects to complete teacher-related use cases
 */
@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final ClassroomAssembler classroomAssembler;

    public TeacherService(TeacherRepository teacherRepository, ClassroomAssembler classroomAssembler) {
        this.teacherRepository = teacherRepository;
        this.classroomAssembler = classroomAssembler;
    }

    /**
     * Register teacher information
     *
     * @param createTeacherCMD@return Teacher ID
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public TeacherDTO registerTeacher(CreateTeacherCMD createTeacherCMD) {
        // Check if user ID is already associated with a teacher
        Optional<Teacher> existingTeacher = teacherRepository.findByUserId(createTeacherCMD.getUserId());
        BizAssert.isTrue(existingTeacher.isEmpty(), "Teacher already exists");
        // Create teacher domain object
        Teacher teacher = new Teacher(createTeacherCMD.getName(), createTeacherCMD.getFaculty(), 
                                    createTeacherCMD.getTitle(), createTeacherCMD.getUserId());
        // Save teacher
        teacherRepository.update(teacher);
        return classroomAssembler.toTeacherDTO(teacher);
    }

    /**
     * Update teacher information
     *
     * @param updateTeacherCMD
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public TeacherDTO updateTeacher(UpdateTeacherCMD updateTeacherCMD) {
        // Get teacher
        Teacher teacher = teacherRepository.findByUserId(updateTeacherCMD.getOperatorId())
            .orElseThrow(BizAssert.failSupplier("Teacher is not found"));
        
        // Update teacher information
        teacher.update(updateTeacherCMD.getName(), updateTeacherCMD.getFaculty(), updateTeacherCMD.getTitle());
        
        // Save teacher
        teacherRepository.create(teacher);
        return classroomAssembler.toTeacherDTO(teacher);
    }

    /**
     * Get teacher by ID
     *
     * @param teacherId Teacher ID
     * @return Teacher object
     */
    public TeacherDTO getTeacherById(Long teacherId) {
        var teacher = teacherRepository.findById(teacherId)
                .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"Teacher is not found"));
        return classroomAssembler.toTeacherDTO(teacher);
    }

    /**
     * Get teacher by user ID
     *
     * @param userId User ID
     * @return Teacher object
     */
    public TeacherDTO getTeacherByUserId(Long userId) {
        var teacher = teacherRepository.findByUserId(userId)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"Teacher is not found"));
        return classroomAssembler.toTeacherDTO(teacher);
    }
}