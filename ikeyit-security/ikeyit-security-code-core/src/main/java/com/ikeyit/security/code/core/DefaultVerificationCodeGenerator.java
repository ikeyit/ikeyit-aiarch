package com.ikeyit.security.code.core;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Default implementation of the VerificationCodeGenerator interface.
 * Generates numeric verification codes of configurable length and lifetime.
 * Uses SecureRandom to ensure the generated codes are cryptographically strong.
 */
public class DefaultVerificationCodeGenerator implements VerificationCodeGenerator {
    /**
     * Secure random number generator for creating verification codes
     */
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * The size (number of digits) of the generated verification code, default is 4
     */
    private int codeSize = 4;

    /**
     * The lifetime of the verification code in seconds, default is 120 (2 minutes)
     */
    private int codeLife = 120;

    /**
     * Generates a random numeric verification code for the specified target.
     * The code will have the configured size and lifetime.
     *
     * @param target The target identifier (e.g., email, phone number) for which to generate the code
     * @return A new VerificationCode object with a randomly generated code and appropriate expiration time
     */
    @Override
    public VerificationCode generate(String target) {
        String code = generateRandomNumberString(codeSize);
        LocalDateTime now = LocalDateTime.now();
        return new VerificationCode(target, code, now, now.plusSeconds(codeLife));
    }

    /**
     * Sets the size (number of digits) for generated verification codes.
     *
     * @param codeSize The code size to set, must be greater than 0
     * @throws IllegalArgumentException if codeSize is less than or equal to 0
     */
    public void setCodeSize(int codeSize) {
        if (codeSize <= 0) {
            throw new IllegalArgumentException("codeSize must be greater than 0");
        }
        this.codeSize = codeSize;
    }

    /**
     * Sets the lifetime in seconds for generated verification codes.
     *
     * @param codeLife The code lifetime in seconds, must be greater than 0
     * @throws IllegalArgumentException if codeLife is less than or equal to 0
     */
    public void setCodeLife(int codeLife) {
        if (codeSize <= 0) {
            throw new IllegalArgumentException("codeLife must be greater than 0");
        }
        this.codeLife = codeLife;
    }

    /**
     * Generates a random string of numbers with the specified length.
     *
     * @param length The length of the random string to generate
     * @return A string of random digits with the specified length
     */
    private static String generateRandomNumberString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); 
        }
        return sb.toString();
    }
}
