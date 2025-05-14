package com.ikeyit.gateway.admin.controller;

import com.ikeyit.gateway.admin.model.SessionVO;
import com.ikeyit.gateway.admin.model.UserVO;
import com.ikeyit.gateway.auth.TokenRefreshService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
public class AdminGatewayController {

    private final TokenRefreshService tokenRefreshService;

    public AdminGatewayController(TokenRefreshService tokenRefreshService) {
        this.tokenRefreshService = tokenRefreshService;
    }

    @RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return "";
    }

    @RequestMapping(value = "/session")
    public Mono<SessionVO> getSession(ServerWebExchange exchange, @AuthenticationPrincipal OAuth2User user) {
        Mono<CsrfToken> csrfTokenMono = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
        return csrfTokenMono
                .map(csrfToken -> buildSessionVO(csrfToken, user));
    }

    private SessionVO buildSessionVO(CsrfToken csrfToken, OAuth2User oAuth2User) {
        SessionVO sessionVO = new SessionVO();
        sessionVO.setCsrfToken(csrfToken.getToken());
        if (oAuth2User != null) {
            UserVO userVO = new UserVO();
            userVO.setId(Long.parseLong(oAuth2User.getName()));
            userVO.setDisplayName(oAuth2User.getAttribute("name"));
            userVO.setAvatar(oAuth2User.getAttribute("picture"));
            sessionVO.setUser(userVO);
        }
        return sessionVO;
    }

    @RequestMapping("/refresh")
    public Mono<Void> refresh(@CurrentSecurityContext SecurityContext securityContext, ServerWebExchange exchange) {
        if (securityContext == null || securityContext.getAuthentication() == null) {
            return Mono.error(new AuthenticationCredentialsNotFoundException("Authentication required"));
        }
        return tokenRefreshService
            .refreshToken((OAuth2AuthenticationToken) securityContext.getAuthentication(), exchange)
            .onErrorResume(OAuth2AuthorizationException.class, error -> {
            if (OAuth2ErrorCodes.INVALID_GRANT.equals(error.getError().getErrorCode())) {
                return exchange.getSession().flatMap(WebSession::invalidate);
            }
            return Mono.error(error);
        });
    }

    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
}
