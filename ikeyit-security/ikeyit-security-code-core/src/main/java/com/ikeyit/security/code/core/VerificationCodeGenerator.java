package com.ikeyit.security.code.core;

/**
 * Interface for generating verification codes.
 * Implementations are responsible for creating verification codes with appropriate
 * expiration times and random code values.
 */
public interface VerificationCodeGenerator {
    /**
     * Generates a new verification code for the specified target.
     *
     * @param target The target identifier (e.g., email, phone number) for which the code is generated
     * @return A new VerificationCode object with a randomly generated code and appropriate expiration time
     */
    VerificationCode generate(String target);
}
