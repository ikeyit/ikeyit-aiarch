package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.common.web.exception.RestExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackageClasses = AccountExceptionHandler.class)
public class AccountExceptionHandler extends RestExceptionHandler {

    public AccountExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}
