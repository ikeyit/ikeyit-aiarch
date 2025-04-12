package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import com.ikeyit.common.web.exception.ErrorRespBuilder;
import com.ikeyit.common.web.exception.RestExceptionHandler;
import com.ikeyit.security.code.core.VerificationCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = AccountExceptionHandler.class)
public class AccountExceptionHandler extends RestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AccountExceptionHandler.class);
    public AccountExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(VerificationCodeException.class)
    public ResponseEntity<ErrorResp> handleVerificationCodeException(VerificationCodeException exception){
        log.error("Error!", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorRespBuilder
                .of(CommonErrorCode.INVALID_ARGUMENT)
                .setMessageSource(messageSource)
                .setMessageKeyPrefix(MESSAGE_KEY_PREFIX)
                .setDefaultMessage(exception.getMessage())
                .build());
    }
}
