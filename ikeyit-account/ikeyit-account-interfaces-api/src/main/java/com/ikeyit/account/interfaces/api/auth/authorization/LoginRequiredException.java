package com.ikeyit.account.interfaces.api.auth.authorization;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class LoginRequiredException extends OAuth2AuthenticationException {


    public LoginRequiredException(String errorCode) {
        super(errorCode);
    }
}
