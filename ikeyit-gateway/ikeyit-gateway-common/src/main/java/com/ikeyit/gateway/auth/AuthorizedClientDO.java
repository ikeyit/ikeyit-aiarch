package com.ikeyit.gateway.auth;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

public class AuthorizedClientDO {

    private String clientRegistrationId;

    private OAuth2AccessToken accessToken;

    private OAuth2RefreshToken refreshToken;

    private String principalName;

    public AuthorizedClientDO() {
    }

    public AuthorizedClientDO(String clientRegistrationId,
                              OAuth2AccessToken accessToken,
                              OAuth2RefreshToken refreshToken,
                              String principalName) {
        this.clientRegistrationId = clientRegistrationId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.principalName = principalName;
    }

    public String getClientRegistrationId() {
        return clientRegistrationId;
    }

    public void setClientRegistrationId(String clientRegistrationId) {
        this.clientRegistrationId = clientRegistrationId;
    }

    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public OAuth2RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(OAuth2RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }
}
