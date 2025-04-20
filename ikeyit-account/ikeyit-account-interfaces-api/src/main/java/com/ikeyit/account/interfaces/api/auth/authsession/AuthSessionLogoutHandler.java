package com.ikeyit.account.interfaces.api.auth.authsession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 *  Clear auth session cookie and remove it from the storage
 */
public class AuthSessionLogoutHandler implements LogoutHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthSessionLogoutHandler.class);

    private final AuthTokenCookieRepository authTokenCookieRepository;

    private final AuthSessionService authSessionService;

    public AuthSessionLogoutHandler(AuthSessionService authSessionService,
                                    AuthTokenCookieRepository authTokenCookieRepository) {
        this.authSessionService = authSessionService;
        this.authTokenCookieRepository = authTokenCookieRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        authTokenCookieRepository.clearCookie(request, response);
        if (authentication instanceof AuthTokenAuthenticationToken authToken) {
            String authSessionId = authToken.getAuthSession().getId();
            logger.debug("Delete auth session from storage: {}", authSessionId);
            authSessionService.deleteAuthSession(authSessionId);
        }
    }
}
