package com.ikeyit.account.application.service;

import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.BizException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class DefaultPasswordValidator implements PasswordValidator {
    private static final Pattern REGEX = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[#?!@$%^&*\\-+_])[a-zA-Z0-9#?!@$%^&*\\-+_]{8,}$");


    @Override
    public void validate(String password) throws BizException {
        BizAssert.hasLength(password, "Password must not be empty");
        BizAssert.isTrue(REGEX.matcher(password).matches(), """
            Password must have at least 8 characters and contains at least one letter, at least one digit and at least one special character.
            """);
    }
}
