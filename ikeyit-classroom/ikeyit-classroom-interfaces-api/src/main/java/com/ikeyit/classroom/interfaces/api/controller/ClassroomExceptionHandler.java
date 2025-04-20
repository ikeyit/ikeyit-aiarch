package com.ikeyit.classroom.interfaces.api.controller;

import com.ikeyit.common.web.exception.RestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackageClasses = ClassroomExceptionHandler.class)
public class ClassroomExceptionHandler extends RestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ClassroomExceptionHandler.class);
    public ClassroomExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}
