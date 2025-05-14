package com.ikeyit.access.interfaces.admin.api.controller;

import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import com.ikeyit.common.web.exception.ErrorRespBuilder;
import com.ikeyit.common.web.exception.RestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = AdminAccessExceptionHandler.class)
public class AdminAccessExceptionHandler extends RestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AdminAccessExceptionHandler.class);
    private final MessageSource messageSource;
    private static final String MESSAGE_KEY_PREFIX = "error.";
    public AdminAccessExceptionHandler(MessageSource messageSource) {
        super(messageSource);
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResp> handleAccessDeniedException(AuthorizationDeniedException exception){
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorRespBuilder
                .of(CommonErrorCode.AUTHORIZATION_REQUIRED)
                .setMessageSource(messageSource)
                .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                .setMessageKey("forbidden")
                .setDefaultMessage(exception.getMessage())
                .build());
    }
}
