package com.ikeyit.account.infrastructure.security.codeauth;

import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.security.code.core.VerificationCode;
import com.ikeyit.security.code.core.VerificationCodeException;
import com.ikeyit.security.code.core.VerificationCodeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class LoginVerificationCodeService implements VerificationCodeService {

    private final VerificationCodeService phoneVerificationCodeService;

    private final VerificationCodeService emailVerificationCodeService;

    public LoginVerificationCodeService(@Qualifier("loginPhoneVerificationCodeService")
                                            VerificationCodeService phoneVerificationCodeService,
                                        @Qualifier("loginEmailVerificationCodeService")
                                            VerificationCodeService emailVerificationCodeService) {
        this.phoneVerificationCodeService = phoneVerificationCodeService;
        this.emailVerificationCodeService = emailVerificationCodeService;
    }

    @Override
    public VerificationCode sendCode(String target) {
        BizAssert.notNull(target, "target is null!");
        if (target.contains("@")) {
            return phoneVerificationCodeService.sendCode(target);
        } else {
            return emailVerificationCodeService.sendCode(target);
        }
    }

    @Override
    public void validate(String target, String code) throws VerificationCodeException {
        BizAssert.notNull(target, "target is null!");
        BizAssert.notNull(code, "code is null!");
        if (target.contains("@")) {
            phoneVerificationCodeService.validate(target, code);
        } else{
            emailVerificationCodeService.validate(target, code);
        }
    }
}
