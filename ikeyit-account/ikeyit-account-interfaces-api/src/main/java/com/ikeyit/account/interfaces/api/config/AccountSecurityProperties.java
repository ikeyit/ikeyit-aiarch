package com.ikeyit.account.interfaces.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.time.Duration;

@ConfigurationProperties("ikeyit.account.security")
public class AccountSecurityProperties {
    private Duration idTokenTimeToLive = Duration.ofMinutes(30);
    private Duration rememberMeTokenTimeToLive  = Duration.ofDays(30);
    private String rememberMeKey = KeyGenerators.string().generateKey();
    private String rememberMeParameter = "remember";
    private String rememberMeCookieName = "rm";

    public Duration getIdTokenTimeToLive() {
        return idTokenTimeToLive;
    }

    public void setIdTokenTimeToLive(Duration idTokenTimeToLive) {
        this.idTokenTimeToLive = idTokenTimeToLive;
    }

    public Duration getRememberMeTokenTimeToLive() {
        return rememberMeTokenTimeToLive;
    }

    public void setRememberMeTokenTimeToLive(Duration rememberMeTokenTimeToLive) {
        this.rememberMeTokenTimeToLive = rememberMeTokenTimeToLive;
    }

    public String getRememberMeKey() {
        return rememberMeKey;
    }

    public void setRememberMeKey(String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    public String getRememberMeParameter() {
        return rememberMeParameter;
    }

    public void setRememberMeParameter(String rememberMeParameter) {
        this.rememberMeParameter = rememberMeParameter;
    }

    public String getRememberMeCookieName() {
        return rememberMeCookieName;
    }

    public void setRememberMeCookieName(String rememberMeCookieName) {
        this.rememberMeCookieName = rememberMeCookieName;
    }
}
