package com.ikeyit.common.exception;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Arrays;


public class BizException extends RuntimeException {

    private ErrorCode errorCode;

    private Object[] args;

    private MessageKey messageKey;

    public BizException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BizException(ErrorCode errorCode, String message, Object... args) {
        this(errorCode, null, null, message, args);
    }

    public BizException(ErrorCode errorCode, MessageKey messageKey, String message, Object... args) {
        this(errorCode, null, messageKey, message, args);
    }

    public BizException(ErrorCode errorCode, Throwable cause, String message, Object... args) {
        this(errorCode, cause, null, message, args);
    }

    public BizException(ErrorCode errorCode, Throwable cause, MessageKey messageKey, String message, Object... args) {
        super(detailMessage(messageKey, message, args), cause);
        this.messageKey = messageKey;
        this.errorCode = errorCode;
        this.args = args;
    }

    private static String detailMessage(MessageKey messageKey, String message, Object... args) {
        if (message != null) {
            return MessageFormat.format(message, args);
        }
        return messageKey != null ? "Key: " + messageKey + ", Args: " + Arrays.toString(args) : null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getErrorCodeValue() {
        return errorCode.value();
    }

    @Nullable
    public MessageKey getMessageKey() {
        return messageKey;
    }

    @Nullable
    public Object[] getArgs() {
        return args;
    }
}
