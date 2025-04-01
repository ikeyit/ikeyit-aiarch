package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.account.infrastructure.security.UserPrincipal;
import com.ikeyit.account.interfaces.api.model.SessionVO;
import com.ikeyit.account.interfaces.api.model.UserVO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {

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
