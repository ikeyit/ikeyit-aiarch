package com.ikeyit.account.interfaces.api.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import com.ikeyit.account.infrastructure.security.UserPrincipal;

import java.io.IOException;
import java.util.Locale;

public class LocaleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final LocaleResolver localeResolver;

    public LocaleAuthenticationSuccessHandler(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            Locale locale = StringUtils.parseLocale(userPrincipal.getLocale());
            localeResolver.setLocale(request, response, locale);
        }
    }
}
