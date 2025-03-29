package com.ikeyit.common.exception;


public class SystemException extends RuntimeException {
    private ErrorCode errorCode;

    public SystemException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
