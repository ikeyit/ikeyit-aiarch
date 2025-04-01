package com.ikeyit.security.code.core;

/**
 * Interface for sending verification codes to users.
 * Implementations are responsible for delivering the verification code to the target
 * through various channels (e.g., email, SMS, console, etc.).
 */
public interface VerificationCodeSender {

    /**
     * Sends a verification code to its target.
     *
     * @param verificationCode The verification code to send
     * @return true if the code was successfully sent, false otherwise
     */
    boolean send(VerificationCode verificationCode);
}
