package com.ikeyit.account.interfaces.api.auth.authsession;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.util.Assert;

public class AuthTokenCookieRepository {

    private static final Logger log = LoggerFactory.getLogger(AuthTokenCookieRepository.class);

    private final String cookieName;

    public AuthTokenCookieRepository() {
        this("asid");
    }

    public AuthTokenCookieRepository(String cookieName) {
        Assert.notNull(cookieName, "Cookie name must not be null!");
        this.cookieName = cookieName;
    }

    public String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                log.debug("Auth session cookie is found: {}", cookie);
                return cookie.getValue();
            }
        }
        log.debug("Auth session cookie is not found!");
        return null;
    }

    public void saveCookie(String token, long maxAge, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Save auth token cookie: {}", token);
        ResponseCookie cookie = ResponseCookie.from(this.cookieName, token)
            .maxAge((int) maxAge)
            .httpOnly(true)
            .secure(request.isSecure())
            .path("/")
            .sameSite("None")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Clear auth token cookie");
        ResponseCookie cookie = ResponseCookie.from(this.cookieName, "")
            .maxAge(0)
            .httpOnly(true)
            .secure(request.isSecure())
            .path("/")
            .sameSite("None")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
