package com.ikeyit.common.web.exception;

import com.ikeyit.common.exception.ErrorCode;
import com.ikeyit.common.exception.ErrorResp;
import com.ikeyit.common.exception.MessageKey;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class ErrorRespBuilder {
    private ErrorCode errorCode;
    private String defaultMessage;
    private String messageKeyPrefix;
    private String messageKey;
    private Object[] args;
    private MessageSource messageSource;

    private ErrorRespBuilder(ErrorCode errorCode) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode is null!");
        }
        this.errorCode = errorCode;
    }

    public ErrorRespBuilder setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public ErrorRespBuilder setMessageKeyPrefix(String messageKeyPrefix) {
        this.messageKeyPrefix = messageKeyPrefix;
        return this;
    }

    public ErrorRespBuilder setMessageKey(String messageKey) {
        this.messageKey = messageKey;
        return this;
    }

    public ErrorRespBuilder setMessageKey(MessageKey messageKey) {
        this.messageKey = messageKey == null ? null : messageKey.value();
        return this;
    }

    public ErrorRespBuilder setArgs(Object... args) {
        this.args = args;
        return this;
    }

    public ErrorRespBuilder setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        return this;
    }

    public ErrorResp build() {
        Locale locale = LocaleContextHolder.getLocale();
        String message = defaultMessage;
        if (messageKey != null && messageSource != null) {
            String fullMessageKey = messageKeyPrefix != null ? messageKeyPrefix + messageKey : messageKey;
            message = messageSource.getMessage(fullMessageKey, args, defaultMessage, locale);
        }
        return new ErrorResp(errorCode.value(), message);
    }


    public static ErrorRespBuilder of(ErrorCode errorCode) {
        return new ErrorRespBuilder(errorCode);
    }
}
