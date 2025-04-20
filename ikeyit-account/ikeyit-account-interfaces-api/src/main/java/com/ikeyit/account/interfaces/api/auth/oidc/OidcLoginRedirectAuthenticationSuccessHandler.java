package com.ikeyit.account.interfaces.api.auth.oidc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class OidcLoginRedirectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private String defaultTargetUrl = "/";

    public OidcLoginRedirectAuthenticationSuccessHandler() {
    }

    public OidcLoginRedirectAuthenticationSuccessHandler(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication
        authentication) throws IOException, ServletException {
        String redirectAfterAuth = (String) request.getAttribute("redirect_after_auth");
        redirectStrategy.sendRedirect(request, response, redirectAfterAuth != null ? redirectAfterAuth : defaultTargetUrl);
    }
}
