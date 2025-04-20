package com.ikeyit.account.interfaces.api.auth.authsession;

public class AuthSessionMissedException extends RuntimeException {
    public AuthSessionMissedException() {
    }

    public AuthSessionMissedException(String message) {
        super(message);
    }

    public AuthSessionMissedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthSessionMissedException(Throwable cause) {
        super(cause);
    }

    public AuthSessionMissedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
