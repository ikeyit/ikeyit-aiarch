package com.ikeyit.account.interfaces.api.auth.authsession;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        Cookie cookie = new Cookie(this.cookieName, token);
        cookie.setMaxAge((int) maxAge);
        cookie.setPath("/");
        cookie.setSecure(request.isSecure());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Clear auth token cookie");
        Cookie cookie = new Cookie(this.cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setSecure(request.isSecure());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
