package com.ikeyit.foo.interfaces.api.controller;

import com.ikeyit.common.web.exception.RestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * <pre>
 * === AI-NOTE ===
 * Generate a FooExceptionHandler extending RestExceptionHandler to handle exceptions thrown by this project
 * === AI-NOTE-END ===
 * </pre>
 */
@ControllerAdvice(basePackageClasses = FooExceptionHandler.class)
public class FooExceptionHandler extends RestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(FooExceptionHandler.class);
    public FooExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}
