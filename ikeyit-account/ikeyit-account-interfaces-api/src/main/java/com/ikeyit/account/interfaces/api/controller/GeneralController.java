package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.account.interfaces.api.model.OidcProviderVO;
import com.ikeyit.account.interfaces.api.model.SessionVO;
import com.ikeyit.account.interfaces.api.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class GeneralController {
    private final List<OidcProviderVO> oidcProviders = new LinkedList<>();

    public GeneralController(@Autowired(required = false) Iterable<ClientRegistration> clientRegistrationIterable) {
        if (clientRegistrationIterable != null) {
            for (ClientRegistration clientRegistration : clientRegistrationIterable) {
                oidcProviders.add(new OidcProviderVO(clientRegistration.getRegistrationId(), clientRegistration.getClientName()));
            }
        }
    }

    @GetMapping({"/api/oidc-providers"})
    public List<OidcProviderVO> getOidcProviders() {
        return oidcProviders;
    }


    /**
     * Retrieves the current user session information including authentication status and CSRF token.
     */
    @GetMapping({"api/session"})
    public SessionVO getSession(@AuthenticationPrincipal UserPrincipal principal, CsrfToken csrfToken) {
        SessionVO sessionVO = new SessionVO();
        if (principal != null) {
            UserVO userVO = new UserVO();
            userVO.setId(principal.getId());
            userVO.setUsername(principal.getUsername());
            userVO.setDisplayName(principal.getDisplayName());
            userVO.setAvatar(principal.getAvatar());
            userVO.setAuthenticated(true);
            sessionVO.setUser(userVO);
        }
        if (csrfToken != null) {
            sessionVO.setCsrfToken(csrfToken.getToken());
        }
        return sessionVO;
    }
}
