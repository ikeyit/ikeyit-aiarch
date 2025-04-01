package com.ikeyit.security.code.core;


import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Default implementation of the VerificationCodeService interface.
 * This service coordinates the generation, storage, and validation of verification codes.
 * It enforces rate limiting for code generation and handles the complete verification code lifecycle.
 */
public class DefaultVerificationCodeService implements VerificationCodeService {

    /**
     * Repository for storing and retrieving verification codes
     */
    private final VerificationCodeRepository verificationCodeRepository;

    /**
     * Sender for delivering verification codes to targets
     */
    private final VerificationCodeSender verificationCodeSender;

    /**
     * Generator for creating new verification codes
     */
    private final VerificationCodeGenerator verificationCodeGenerator;

    /**
     * Minimum interval in seconds between sending codes to the same target, default is 60 seconds
     */
    private int resendInterval = 60;

    /**
     * Constructs a new DefaultVerificationCodeService with the specified dependencies.
     *
     * @param verificationCodeRepository Repository for storing and retrieving verification codes
     * @param verificationCodeSender Sender for delivering verification codes to targets
     * @param verificationCodeGenerator Generator for creating new verification codes
     */
    public DefaultVerificationCodeService(VerificationCodeRepository verificationCodeRepository,
                                          VerificationCodeSender verificationCodeSender,
                                          VerificationCodeGenerator verificationCodeGenerator) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.verificationCodeSender = verificationCodeSender;
        this.verificationCodeGenerator = verificationCodeGenerator;
    }

    /**
     * Gets the minimum interval in seconds between sending codes to the same target.
     *
     * @return The resend interval in seconds
     */
    public int getResendInterval() {
        return resendInterval;
    }

    /**
     * Sets the minimum interval in seconds between sending codes to the same target.
     *
     * @param resendInterval The resend interval in seconds
     */
    public void setResendInterval(int resendInterval) {
        this.resendInterval = resendInterval;
    }

    /**
     * Validates if the target is in a valid format.
     * This method can be overridden by subclasses to implement specific validation rules.
     *
     * @param target The target to validate
     * @return true if the target is valid, false otherwise
     */
    protected boolean validateTarget(String target) {
        return true;
    }

    /**
     * Generates and sends a verification code to the specified target.
     * Enforces rate limiting to prevent too frequent requests for the same target.
     *
     * @param target The target identifier to send the code to
     * @return The generated verification code
     * @throws VerificationCodeException If the target is invalid, sending fails, or if a code was recently sent
     */
    @Override
    public VerificationCode sendCode(String target) {
        if (!validateTarget(target))
            throw new VerificationCodeException("The target is invalid!");

        VerificationCode verificationCode = verificationCodeRepository.get(target);
        LocalDateTime now = LocalDateTime.now();
        if (verificationCode != null && now.isBefore(verificationCode.getCreateTime().plusSeconds(resendInterval)))
            throw new VerificationCodeException("Too frequently");

        verificationCode = verificationCodeGenerator.generate(target);
        verificationCodeRepository.save(verificationCode);
        if (!verificationCodeSender.send(verificationCode))
            throw new VerificationCodeException("Failed to send verification code!");
        return verificationCode;
    }


    /**
     * Validates a verification code against the stored code for the specified target.
     * If validation is successful, the code is deleted to prevent reuse.
     *
     * @param target The target identifier to validate the code for
     * @param code The verification code to validate
     * @throws VerificationCodeException If the target is invalid, the code is incorrect, expired, or not found
     */
    @Override
    public void validate(String target, String code) throws VerificationCodeException {
        if (!validateTarget(target))
            throw new VerificationCodeException("The target is invalid!");

        if (!StringUtils.hasText(code))
            throw new VerificationCodeException("Verification code is empty");

        VerificationCode verificationCode = verificationCodeRepository.get(target);

        if (verificationCode == null)
            throw new VerificationCodeException("Verification code is not found or has expired!");

        if (!verificationCode.verify(code))
            throw new VerificationCodeException("Verification code is not correct!");

        if (verificationCode.hasExpired())
            throw new VerificationCodeException("Verification code has expired!");

        verificationCodeRepository.deleteByTarget(target);
    }

}
