package com.ikeyit.security.code.core;

/**
 * Exception thrown when verification code operations fail.
 * This exception is used to indicate various error conditions in the verification code system,
 * such as invalid targets, incorrect codes, expired codes, or sending failures.
 */
public class VerificationCodeException extends RuntimeException {

    /**
     * Constructs a new verification code exception with null as its detail message.
     */
    public VerificationCodeException() {
    }

    /**
     * Constructs a new verification code exception with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception
     */
    public VerificationCodeException(String message) {
        super(message);
    }
}
