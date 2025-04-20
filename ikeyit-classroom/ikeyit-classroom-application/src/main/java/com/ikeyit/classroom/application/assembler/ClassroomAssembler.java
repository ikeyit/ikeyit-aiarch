package com.ikeyit.classroom.application.assembler;

import com.ikeyit.classroom.application.model.*;
import com.ikeyit.classroom.domain.model.*;
import org.springframework.stereotype.Component;

/**
 * 课程数据转换器
 * 负责在领域模型和DTO之间进行转换
 */
@Component
public class ClassroomAssembler {

    /**
     * 将领域模型转换为DTO
     *
     * @param course 课程领域模型
     * @return 课程DTO
     */
    public CourseDTO toCourseDTO(Course course, Teacher teacher) {
        if (course == null) {
            return null;
        }
        
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setImageUrl(course.getImageUrl());
        dto.setCredits(course.getCredits());
        dto.setTeacherUserId(course.getTeacherUserId());
        dto.setStartDate(course.getStartDate());
        dto.setEndDate(course.getEndDate());
        dto.setStatus(course.getStatus());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        if (teacher != null) {
            dto.setTeacher(toTeacherDTO(teacher));
        }
        return dto;
    }

    public StudentDTO toStudentDTO(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setStudentNo(student.getStudentNo());
        dto.setFaculty(student.getFaculty());
        dto.setClassName(student.getClassName());
        dto.setUserId(student.getUserId());

        return dto;
    }


    /**
     * 将领域模型转换为DTO
     *
     * @param teacher 教师领域模型
     * @return 教师DTO
     */
    public TeacherDTO toTeacherDTO(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName());
        dto.setFaculty(teacher.getFaculty());
        dto.setTitle(teacher.getTitle());
        dto.setUserId(teacher.getUserId());

        return dto;
    }

    /**
     * 将领域模型转换为DTO
     *
     * @param classSchedule 课程表领域模型
     * @return 课程表DTO
     */
    public ClassScheduleDTO toClassScheduleDTO(ClassSchedule classSchedule) {
        if (classSchedule == null) {
            return null;
        }

        ClassScheduleDTO dto = new ClassScheduleDTO();
        dto.setId(classSchedule.getId());
        dto.setCourseId(classSchedule.getCourseId());
        dto.setClassroom(classSchedule.getClassroom());
        dto.setStartTime(classSchedule.getStartTime());
        dto.setEndTime(classSchedule.getEndTime());

        return dto;
    }

    /**
     * 将领域模型转换为DTO
     *
     * @param enrollment 选课领域模型
     * @return 选课DTO
     */
    public EnrollmentDTO toEnrollmentDTO(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentUserId(enrollment.getStudentUserId());
        dto.setCourseId(enrollment.getCourseId());
        dto.setStatus(enrollment.getStatus());
        dto.setEnrollDate(enrollment.getEnrollDate());

        return dto;
    }

}