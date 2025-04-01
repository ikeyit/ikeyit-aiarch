package com.ikeyit.account.application.service;

import com.ikeyit.common.exception.BizException;

public interface PasswordValidator {
    void validate(String password) throws BizException;
}
