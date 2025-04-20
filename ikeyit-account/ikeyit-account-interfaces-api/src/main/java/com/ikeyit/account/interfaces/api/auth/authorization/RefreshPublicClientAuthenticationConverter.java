package com.ikeyit.account.interfaces.api.auth.authorization;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Enable refresh token for public clients.
 */
public final class RefreshPublicClientAuthenticationConverter implements AuthenticationConverter {
	private static final Logger log = LoggerFactory.getLogger(RefreshPublicClientAuthenticationConverter.class);
	@Nullable
	@Override
	public Authentication convert(HttpServletRequest request) {
		// When it's a refresh token request, we allow a public client without secret
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		String secret = request.getParameter(OAuth2ParameterNames.CLIENT_SECRET);
		if (!AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantType) || StringUtils.hasText(secret)) {
			return null;
		}
		String refreshTokenParam = request.getParameter(OAuth2ParameterNames.REFRESH_TOKEN);
		if (!StringUtils.hasLength(refreshTokenParam)) {
			throw new OAuth2AuthenticationException("refresh_token is require!");
		}

		String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
		if (!StringUtils.hasLength(clientId)) {
			throw new OAuth2AuthenticationException("client_id is require!");
		}

		Map<String, Object> additionalParameters = new HashMap<>();
		for (Map.Entry<String, String[]> entry: request.getParameterMap().entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (values != null) {
				if (values.length == 1) {
					additionalParameters.put(key, values[0]);
				} else {
					additionalParameters.put(key, values);
				}
			}
		}
		additionalParameters.remove(OAuth2ParameterNames.CLIENT_ID);
		return new OAuth2ClientAuthenticationToken(clientId, ClientAuthenticationMethod.NONE, null, additionalParameters);
	}
}
