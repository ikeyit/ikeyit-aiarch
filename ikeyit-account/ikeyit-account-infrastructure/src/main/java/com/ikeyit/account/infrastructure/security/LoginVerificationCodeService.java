package com.ikeyit.account.infrastructure.security;

import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.security.code.core.VerificationCode;
import com.ikeyit.security.code.core.VerificationCodeException;
import com.ikeyit.security.code.core.VerificationCodeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class LoginVerificationCodeService implements VerificationCodeService {

    private final VerificationCodeService mobileVerificationCodeService;

    private final VerificationCodeService emailVerificationCodeService;

    public LoginVerificationCodeService(@Qualifier("loginMobileVerificationCodeService")
                                            VerificationCodeService mobileVerificationCodeService,
                                        @Qualifier("loginEmailVerificationCodeService")
                                            VerificationCodeService emailVerificationCodeService) {
        this.mobileVerificationCodeService = mobileVerificationCodeService;
        this.emailVerificationCodeService = emailVerificationCodeService;
    }

    @Override
    public VerificationCode sendCode(String target) {
        BizAssert.notNull(target, "target is null!");
        if (target.contains("@")) {
            return mobileVerificationCodeService.sendCode(target);
        } else {
            return emailVerificationCodeService.sendCode(target);
        }
    }

    @Override
    public void validate(String target, String code) throws VerificationCodeException {
        BizAssert.notNull(target, "target is null!");
        BizAssert.notNull(code, "code is null!");
        if (target.contains("@")) {
            mobileVerificationCodeService.validate(target, code);
        } else{
            emailVerificationCodeService.validate(target, code);
        }
    }
}
