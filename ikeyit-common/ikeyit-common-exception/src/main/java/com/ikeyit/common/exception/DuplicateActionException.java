package com.ikeyit.common.exception;

/**
 * Exception thrown when a duplicate action is detected.
 * This exception is used to indicate that an operation that should be unique has been attempted multiple times.
 */
public class DuplicateActionException extends RuntimeException {
    
    /**
     * Creates a new DuplicateActionException with no message.
     */
    public DuplicateActionException() {
    }

    /**
     * Creates a new DuplicateActionException with the specified message.
     * @param message The detail message
     */
    public DuplicateActionException(String message) {
        super(message);
    }
}
