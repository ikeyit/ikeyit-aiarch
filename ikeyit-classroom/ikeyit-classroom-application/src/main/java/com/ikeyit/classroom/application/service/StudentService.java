package com.ikeyit.classroom.application.service;

import com.ikeyit.classroom.application.assembler.ClassroomAssembler;
import com.ikeyit.classroom.application.model.CreateStudentCMD;
import com.ikeyit.classroom.application.model.StudentDTO;
import com.ikeyit.classroom.application.model.UpdateStudentCMD;
import com.ikeyit.classroom.domain.model.Student;
import com.ikeyit.classroom.domain.repository.StudentRepository;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Student Application Service
 * Responsible for coordinating domain objects to complete student-related use cases
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final ClassroomAssembler classroomAssembler;

    public StudentService(StudentRepository studentRepository, ClassroomAssembler classroomAssembler) {
        this.studentRepository = studentRepository;
        this.classroomAssembler = classroomAssembler;
    }

    /**
     * Register student information
     *
     * @param createStudentCMD@return Student ID
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public StudentDTO registerStudent(CreateStudentCMD createStudentCMD) {
        // Check if student ID already exists
        Optional<Student> existingStudent = studentRepository.findByStudentNo(createStudentCMD.getStudentNo());
        BizAssert.isTrue(existingStudent.isEmpty(), "Student ID already exists");
        // Check if user ID is already associated with a student
        Optional<Student> existingUserStudent = studentRepository.findByUserId(createStudentCMD.getUserId());
        BizAssert.isTrue(existingUserStudent.isEmpty(), "This user is already associated with student information");
        // Create student domain object
        Student student = new Student(createStudentCMD.getName(), createStudentCMD.getStudentNo(),
                                    createStudentCMD.getFaculty(), createStudentCMD.getClassName(), 
                                    createStudentCMD.getUserId());
        // Save student
        studentRepository.update(student);
        return classroomAssembler.toStudentDTO(student);
    }

    /**
     * Update student information
     *
     * @param updateStudentCMD
     */
    @Transactional(transactionManager = "classroomTransactionManager")
    public StudentDTO updateStudent(UpdateStudentCMD updateStudentCMD) {
        // Get student
        Student student = studentRepository.findByUserId(updateStudentCMD.getOperatorId())
            .orElseThrow(BizAssert.failSupplier("Student is not found"));
        
        // Update student information
        student.update(updateStudentCMD.getName(), updateStudentCMD.getFaculty(), updateStudentCMD.getClassName());
        
        // Save student
        studentRepository.update(student);
        return classroomAssembler.toStudentDTO(student);
    }

    /**
     * Get student by ID
     *
     * @param studentId Student ID
     * @return Student object
     */
    public StudentDTO getStudentById(Long studentId) {
        var student = studentRepository.findById(studentId)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"Student is not found"));
        return classroomAssembler.toStudentDTO(student);
    }

    /**
     * Get student by user ID
     *
     * @param userId User ID
     * @return Student object
     */
    public StudentDTO getStudentByUserId(Long userId) {
        var student = studentRepository.findByUserId(userId)
            .orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND,"Student is not found"));
        return classroomAssembler.toStudentDTO(student);
    }
}