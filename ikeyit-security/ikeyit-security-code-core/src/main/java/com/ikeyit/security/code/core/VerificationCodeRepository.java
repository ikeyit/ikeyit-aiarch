package com.ikeyit.security.code.core;


/**
 * Repository interface for storing and retrieving verification codes.
 * Implementations are responsible for the persistence of verification codes,
 * allowing them to be saved, retrieved, and deleted.
 */
public interface VerificationCodeRepository {

    /**
     * Saves a verification code to the repository.
     * If a code already exists for the same target, it will be overwritten.
     *
     * @param verificationCode The verification code to save
     */
    void save(VerificationCode verificationCode);

    /**
     * Retrieves a verification code for the specified target.
     *
     * @param target The target identifier (e.g., email, phone number) to look up
     * @return The verification code if found, or null if no code exists for the target
     */
    VerificationCode get(String target);

    /**
     * Deletes a verification code for the specified target.
     * This is typically called after successful validation to prevent reuse.
     *
     * @param target The target identifier for which to delete the verification code
     */
    void deleteByTarget(String target);
}
