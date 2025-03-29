package com.ikeyit.common.exception;

public class DuplicateActionException extends RuntimeException {
    public DuplicateActionException() {
    }

    public DuplicateActionException(String message) {
        super(message);
    }
}
