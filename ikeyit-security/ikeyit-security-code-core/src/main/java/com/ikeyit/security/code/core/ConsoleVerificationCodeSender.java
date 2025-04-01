package com.ikeyit.security.code.core;


/**
 * Implementation of VerificationCodeSender that outputs verification codes to the console.
 * This implementation is primarily useful for testing and development environments
 * where actual delivery mechanisms (SMS, email, etc.) are not available or needed.
 */
public class ConsoleVerificationCodeSender implements VerificationCodeSender {
    /**
     * The name identifier for this sender, used in console output
     */
    private final String name;

    /**
     * Constructs a new console verification code sender with a default name.
     */
    public ConsoleVerificationCodeSender() {
        this("DefaultVerificationCodeSender");
    }

    /**
     * Constructs a new console verification code sender with the specified name.
     *
     * @param name The name identifier for this sender, used in console output
     */
    public ConsoleVerificationCodeSender(String name) {
        this.name = name;
    }

    /**
     * Sends the verification code by printing it to the console.
     * This method always returns true as console output rarely fails.
     *
     * @param verificationCode The verification code to send
     * @return true indicating the code was successfully sent to console
     */
    @Override
    public boolean send(VerificationCode verificationCode) {
        System.out.println(name + ", Target: " + verificationCode.getTarget() + ", Verification Code: " + verificationCode.getCode());
        return true;
    }
}
