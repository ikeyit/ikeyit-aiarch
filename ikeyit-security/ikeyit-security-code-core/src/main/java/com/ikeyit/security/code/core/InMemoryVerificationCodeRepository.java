package com.ikeyit.security.code.core;

import java.util.HashMap;

/**
 * In-memory implementation of the VerificationCodeRepository interface.
 * This implementation stores verification codes in a HashMap, making it suitable
 * for testing or applications with low persistence requirements.
 * Note that codes are lost when the application restarts.
 */
public class InMemoryVerificationCodeRepository implements VerificationCodeRepository {

    /**
     * HashMap that stores verification codes with target identifiers as keys
     */
    HashMap<String, VerificationCode> codes = new HashMap<>();

    /**
     * Constructs a new empty in-memory verification code repository
     */
    public InMemoryVerificationCodeRepository() {
    }

    /**
     * Saves a verification code to the in-memory repository.
     * If a code already exists for the same target, it will be overwritten.
     *
     * @param verificationCode The verification code to save
     */
    @Override
    public void save(VerificationCode verificationCode) {
        codes.put(verificationCode.getTarget(), verificationCode);
    }

    /**
     * Retrieves a verification code for the specified target from the in-memory repository.
     *
     * @param target The target identifier to look up
     * @return The verification code if found, or null if no code exists for the target
     */
    @Override
    public VerificationCode get(String target) {
        return codes.get(target);
    }


    /**
     * Deletes a verification code for the specified target from the in-memory repository.
     *
     * @param target The target identifier for which to delete the verification code
     */
    @Override
    public void deleteByTarget(String target) {
        codes.remove(target);
    }
}
