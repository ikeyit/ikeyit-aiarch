package com.ikeyit.account.interfaces.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.util.UriComponentsBuilder;

public class RedirectAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private String redirectParameter = "redirect";

    public RedirectAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    public void setRedirectParameter(String redirectParameter) {
        this.redirectParameter = redirectParameter;
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        return UriComponentsBuilder.fromPath(getLoginFormUrl())
            .queryParam(redirectParameter, UrlUtils.buildFullRequestUrl(request))
            .toUriString();
    }
}
