package com.ikeyit.account.application.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.BizException;
import com.ikeyit.common.exception.CommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Implementation of ContactInfoValidator that provides country-specific validation for phone numbers
 * using Google's libphonenumber library.
 */
@Service
public class DefaultContactInfoValidator implements ContactInfoValidator {
    
    private static final Logger log = LoggerFactory.getLogger(DefaultContactInfoValidator.class);
    
    // RFC 5322 compliant email regex pattern
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    
    // PhoneNumberUtil instance for validating phone numbers
    private final PhoneNumberUtil phoneNumberUtil;
    
    public DefaultContactInfoValidator() {
        this.phoneNumberUtil = PhoneNumberUtil.getInstance();
    }
    
    @Override
    public void validateEmail(String email) throws BizException {
        BizAssert.hasLength(email, "Email must not be empty");
        BizAssert.isTrue(EMAIL_REGEX.matcher(email).matches(), 
                "Invalid email format. Please provide a valid email address.");
    }
    
    @Override
    public void validatePhone(String phone) throws BizException {
        BizAssert.hasLength(phone, "Phone number must not be empty");
        // Ensure the phone number starts with a plus sign for international format
        BizAssert.isTrue(phone.startsWith("+"),
                "Phone number must be in international format starting with '+' followed by country code");
        try {
            // Parse the phone number
            PhoneNumber phoneNumber = phoneNumberUtil.parse(phone, null);
            
            // Check if the number is valid
            BizAssert.isTrue(phoneNumberUtil.isValidNumber(phoneNumber),
                "Invalid phone number for country code +" + phoneNumber.getCountryCode());
            // Check if the number is a phone number
            PhoneNumberUtil.PhoneNumberType numberType = phoneNumberUtil.getNumberType(phoneNumber);
            BizAssert.isTrue(numberType == PhoneNumberUtil.PhoneNumberType.MOBILE ||
                    numberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE,
                "The provided number is not a phone number. Please provide a valid phone number.");
        } catch (NumberParseException e) {
            log.debug("Failed to parse phone number: {}", e.getMessage());
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, 
                    "Invalid phone number format. Please provide a valid international phone number in E.164 format (e.g., +12345678901).");
        }
    }
}