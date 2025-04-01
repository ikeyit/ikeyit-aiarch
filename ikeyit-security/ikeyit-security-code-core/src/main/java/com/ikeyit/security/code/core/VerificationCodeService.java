package com.ikeyit.security.code.core;

/**
 * Service interface for verification code operations.
 * This service provides methods for generating, sending, and validating verification codes.
 * It acts as the main entry point for the verification code system.
 */
public interface VerificationCodeService {

    /**
     * Generates and sends a verification code to the specified target.
     * This method handles the generation, storage, and delivery of the code.
     *
     * @param target The target identifier (e.g., email, phone number) to send the code to
     * @return The generated verification code object
     * @throws VerificationCodeException If the target is invalid, sending fails, or if a code was recently sent
     */
    VerificationCode sendCode(String target);

    /**
     * Validates a verification code against the stored code for the specified target.
     * If validation is successful, the code is deleted to prevent reuse.
     *
     * @param target The target identifier (e.g., email, phone number) to validate the code for
     * @param code The verification code to validate
     * @throws VerificationCodeException If the target is invalid, the code is incorrect, expired, or not found
     */
    void validate(String target, String code) throws VerificationCodeException;
}
