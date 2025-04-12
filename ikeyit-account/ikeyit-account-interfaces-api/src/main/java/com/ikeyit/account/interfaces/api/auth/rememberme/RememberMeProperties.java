package com.ikeyit.account.interfaces.api.auth.rememberme;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.time.Duration;

@ConfigurationProperties("spring.security.remember-me")
public class RememberMeProperties {
    private boolean enabled = true;
    private Duration tokenValiditySeconds  = Duration.ofDays(30);
    private String key = KeyGenerators.string().generateKey();
    private String parameter = "remember";
    private String cookieName = "rm";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Duration getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }

    public void setTokenValiditySeconds(Duration tokenValiditySeconds) {
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
