package com.ikeyit.account.interfaces.api.auth.authorization;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthorizationCodeRequestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeRequestAuthenticationFailureHandler.class);
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String loginPageUrl;
    private final String authorizationBaseUrl;
        ;
    public AuthorizationCodeRequestAuthenticationFailureHandler(
        String loginPageUrl,
        String authorizationBaseUrl) {
        this.loginPageUrl = loginPageUrl;
        this.authorizationBaseUrl = authorizationBaseUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof LoginRequiredException) {
            String loginMethod = request.getParameter("login_method");
            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(request.getRequestURL().toString())
                .query(request.getQueryString())
                .replaceQueryParam("login_method")
                .replaceQueryParam("prompt");
            String redirectUrl = urlBuilder
                .build(true)
                .toUriString();
            String url = null;
            if (StringUtils.hasText(loginMethod) && loginMethod.startsWith("oidc-")) {
                String providerId = loginMethod.substring("oidc-".length());
                url = UriComponentsBuilder.fromPath(authorizationBaseUrl + "/" + providerId)
                    .queryParam("redirect", redirectUrl)
                    .toUriString();
            } else {
                url = UriComponentsBuilder.fromPath(loginPageUrl)
                    .queryParam("redirect", redirectUrl)
                    .queryParam("mode", "force")
                    .toUriString();
            }
            this.redirectStrategy.sendRedirect(request, response, url);
        } else {
            sendErrorResponse(request, response, exception);
        }
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        OAuth2AuthorizationCodeRequestAuthenticationException authorizationCodeRequestAuthenticationException = (OAuth2AuthorizationCodeRequestAuthenticationException) exception;
        OAuth2Error error = authorizationCodeRequestAuthenticationException.getError();
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = authorizationCodeRequestAuthenticationException
            .getAuthorizationCodeRequestAuthentication();

        if (authorizationCodeRequestAuthentication == null
            || !StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), error.toString());
            return;
        }
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromUriString(authorizationCodeRequestAuthentication.getRedirectUri())
            .queryParam(OAuth2ParameterNames.ERROR, error.getErrorCode());
        if (StringUtils.hasText(error.getDescription())) {
            uriBuilder.queryParam(OAuth2ParameterNames.ERROR_DESCRIPTION,
                UriUtils.encode(error.getDescription(), StandardCharsets.UTF_8));
        }
        if (StringUtils.hasText(error.getUri())) {
            uriBuilder.queryParam(OAuth2ParameterNames.ERROR_URI,
                UriUtils.encode(error.getUri(), StandardCharsets.UTF_8));
        }
        if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
            uriBuilder.queryParam(OAuth2ParameterNames.STATE,
                UriUtils.encode(authorizationCodeRequestAuthentication.getState(), StandardCharsets.UTF_8));
        }
        // build(true) -> Components are explicitly encoded
        String redirectUri = uriBuilder.build(true).toUriString();
        this.redirectStrategy.sendRedirect(request, response, redirectUri);
    }

}
