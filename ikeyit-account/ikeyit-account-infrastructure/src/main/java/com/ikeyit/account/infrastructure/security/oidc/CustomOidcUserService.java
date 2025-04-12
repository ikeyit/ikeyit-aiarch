package com.ikeyit.account.infrastructure.security.oidc;

import com.ikeyit.account.application.model.ConnectUserCMD;
import com.ikeyit.account.application.service.UserConnectionService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserConnectionService userConnectionService;

    public CustomOidcUserService(UserConnectionService userConnectionService) {
        this.userConnectionService = userConnectionService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        var connectUserCMD = getConnectUserCMD(userRequest, oidcUser);
        var user = userConnectionService.loadOrCreateUserByConnection(connectUserCMD);
        return new OidcUserPrincipal(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getDisplayName(),
            user.getAvatar(),
            user.getLocale(),
            user.getEmail(),
            user.getPhone(),
            user.getRoles(),
            user.isEnabled(),
            true,
            true,
            true);
    }

    private static ConnectUserCMD getConnectUserCMD(OidcUserRequest userRequest, OidcUser oidcUser) {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        var connectUserCMD = new ConnectUserCMD();
        connectUserCMD.setProvider(provider);
        connectUserCMD.setSub(oidcUser.getSubject());
        connectUserCMD.setName(oidcUser.getFullName());
        connectUserCMD.setNickname(oidcUser.getNickName());
        connectUserCMD.setEmail(oidcUser.getEmail());
        connectUserCMD.setPhoneNumber(oidcUser.getPhoneNumber());
        connectUserCMD.setPicture(oidcUser.getPicture());
        connectUserCMD.setPreferredUsername(oidcUser.getPreferredUsername());
        return connectUserCMD;
    }
}