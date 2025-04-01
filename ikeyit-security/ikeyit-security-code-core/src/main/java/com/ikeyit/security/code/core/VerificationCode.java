package com.ikeyit.security.code.core;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a verification code entity with its associated metadata.
 * This class encapsulates all information related to a verification code,
 * including the target it was generated for, the code itself, and its
 * creation and expiration times.
 */
public class VerificationCode {

    /**
     * The target identifier (e.g., email, phone number) this verification code is for
     */
    private String target;

    /**
     * The actual verification code value
     */
    private String code;

    /**
     * The time when this verification code was created
     */
    private LocalDateTime createTime;

    /**
     * The time when this verification code will expire
     */
    private LocalDateTime expireTime;

    /**
     * Default constructor for serialization/deserialization purposes
     */
    public VerificationCode() {
    }

    /**
     * Creates a new verification code with the specified parameters
     *
     * @param target The target identifier this code is for
     * @param code The verification code value
     * @param createTime The time when this code was created
     * @param expireTime The time when this code will expire
     */
    public VerificationCode(String target, String code, LocalDateTime createTime, LocalDateTime expireTime) {
        this.target = target;
        this.code = code;
        this.createTime = createTime;
        this.expireTime = expireTime;
    }

    /**
     * Gets the target identifier this verification code is for
     *
     * @return The target identifier (e.g., email, phone number)
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target identifier for this verification code
     *
     * @param target The target identifier to set
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Gets the verification code value
     *
     * @return The verification code string
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the verification code value
     *
     * @param code The verification code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the expiration time of this verification code
     *
     * @return The expiration time
     */
    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    /**
     * Sets the expiration time for this verification code
     *
     * @param expireTime The expiration time to set
     */
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * Gets the creation time of this verification code
     *
     * @return The creation time
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * Sets the creation time for this verification code
     *
     * @param createTime The creation time to set
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * Checks if this verification code has expired at the specified time
     *
     * @param time The time to check against
     * @return true if the code has expired, false otherwise
     */
    public boolean hasExpired(LocalDateTime time) {
        return expireTime != null && expireTime.isBefore(time);
    }

    /**
     * Checks if this verification code has expired at the current time
     *
     * @return true if the code has expired, false otherwise
     */
    public boolean hasExpired() {
        return hasExpired(LocalDateTime.now());
    }

    /**
     * Verifies if the provided code matches this verification code
     *
     * @param code The code to verify against this verification code
     * @return true if the codes match, false otherwise
     */
    public boolean verify(String code) {
        return Objects.equals(this.code, code);
    }

    @Override
    public String toString() {
        return "VerificationCode{" +
            "target='" + target + '\'' +
            ", code='" + code + '\'' +
            ", createTime=" + createTime +
            ", expireTime=" + expireTime +
            '}';
    }
}
