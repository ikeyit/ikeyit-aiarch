package com.ikeyit.account.application.service;

import com.ikeyit.common.exception.BizException;

/**
 * Interface for validating contact information like email addresses and phone numbers
 */
public interface ContactInfoValidator {
    /**
     * Validates an email address
     * @param email The email address to validate
     * @throws BizException if the email is invalid
     */
    void validateEmail(String email) throws BizException;
    
    /**
     * Validates a phone number
     * @param phone The phone number to validate
     * @throws BizException if the phone number is invalid
     */
    void validatePhone(String phone) throws BizException;
}