package com.ikeyit.common.exception;

/**
 * Runtime exception for system-level errors.
 * This exception is used to indicate errors that occur at the system level rather than business logic level.
 */
public class SystemException extends RuntimeException {
    private ErrorCode errorCode;

    /**
     * Creates a new SystemException with the specified error code, message, and cause.
     * @param errorCode The error code for this exception
     * @param message The detail message
     * @param cause The cause of this exception
     */
    public SystemException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Creates a new SystemException with the specified error code and message.
     * @param errorCode The error code for this exception
     * @param message The detail message
     */
    public SystemException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    /**
     * Gets the error code associated with this exception.
     * @return The error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
