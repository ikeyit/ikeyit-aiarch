package com.ikeyit.account.interfaces.api.auth.authsession;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(LoginAuthenticationSuccessHandler.class);

    private final AuthSessionService authSessionService;

    private final AuthTokenGenerator authTokenGenerator;

    private final AuthTokenCookieRepository authTokenCookieRepository;

    public LoginAuthenticationSuccessHandler(AuthSessionService authSessionService,
                                             AuthTokenGenerator authTokenGenerator,
                                             AuthTokenCookieRepository authTokenCookieRepository) {
        this.authSessionService = authSessionService;
        this.authTokenGenerator = authTokenGenerator;
        this.authTokenCookieRepository = authTokenCookieRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("Save auth session and token!");
        AuthSession authSession = authSessionService.createAuthSession(authentication);
        if (authSession != null) {
            String token = authTokenGenerator.generateToken(authSession);
            authTokenCookieRepository.saveCookie(token, authSession.duration().getSeconds(), request, response);
        }
    }
}
