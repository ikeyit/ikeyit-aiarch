package com.ikeyit.account.interfaces.api.auth.oidc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ikeyit.common.data.JsonUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

/**
 * Before request oauth2 provider to authorize, the OAuth2AuthorizationRequest need to be stored. After the authorization,
 * the OAuth2AuthorizationRequest will be retrieved to continue the flow. By default, OAuth2AuthorizationRequest is store
 * in session, which means our app must be stateful. In order to be stateless, we store the OAuth2AuthorizationRequest
 * in cookie and encrypted.
 */
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    static final TypeReference<Set<String>> STRING_SET = new TypeReference<>() {
    };

    static final TypeReference<Map<String, Object>> STRING_OBJECT_MAP = new TypeReference<>() {
    };

    private static final Logger log = LoggerFactory.getLogger(CookieAuthorizationRequestRepository.class);

    private static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "oar";

    private static final int COOKIE_EXPIRATION_SECONDS = 60;

    private int cookieMaxAge = COOKIE_EXPIRATION_SECONDS;

    private String cookieName = AUTHORIZATION_REQUEST_COOKIE_NAME;

    private final BytesEncryptor encryptor;

    public CookieAuthorizationRequestRepository() {
        this(KeyGenerators.string().generateKey());
    }

    public CookieAuthorizationRequestRepository(String password) {
        String salt = KeyGenerators.string().generateKey();
        encryptor = Encryptors.standard(password, salt);
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public void setCookieName(String cookieName) {
        Assert.hasText(cookieName, "cookieName must not be empty!");
        this.cookieName = cookieName;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    // Deserialize the cookie value into an OAuth2AuthorizationRequest object
                    return decrypt(cookie.getValue());
                }
            }
        }
        return null;
    }


    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }
        String serializedValue = encrypt(authorizationRequest);
        Cookie cookie = new Cookie(cookieName, serializedValue);
        cookie.setPath("/");
        cookie.setSecure(request.isSecure());
        cookie.setHttpOnly(true); // Mark cookie as HttpOnly
        cookie.setMaxAge(cookieMaxAge);
        response.addCookie(cookie);
    }


    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest originalRequest = loadAuthorizationRequest(request);
        if (originalRequest != null) {
            removeAuthorizationRequestCookies(request, response);
        }
        return originalRequest;
    }

    private void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    private OAuth2AuthorizationRequest deserialize(String value) {
        log.debug("OAuth2AuthorizationRequest from cookie: {}", value);
        ObjectMapper objectMapper = JsonUtils.mapper();
        ObjectNode root = JsonUtils.readValue(value, ObjectNode.class);
        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode();
        builder.authorizationUri(root.path("authorizationUri").asText());
        builder.clientId(root.path("clientId").asText());
        builder.redirectUri(root.path("redirectUri").asText());
        builder.scopes(objectMapper.convertValue(root.path("scopes"), STRING_SET));
        builder.state(root.path("state").asText());
        builder.additionalParameters(objectMapper.convertValue(root.path("additionalParameters"), STRING_OBJECT_MAP));
        builder.authorizationRequestUri(root.path("authorizationRequestUri").asText());
        builder.attributes(objectMapper.convertValue(root.path("attributes"), STRING_OBJECT_MAP));
        return builder.build();
    }


    private String serialize(OAuth2AuthorizationRequest authorizationRequest)  {
        ObjectMapper objectMapper = JsonUtils.mapper();
        ObjectNode root = objectMapper.createObjectNode();
        root.put("authorizationUri", authorizationRequest.getAuthorizationUri());
        root.put("clientId", authorizationRequest.getClientId());
        root.put("redirectUri", authorizationRequest.getRedirectUri());
        root.set("scopes", objectMapper.valueToTree(authorizationRequest.getScopes()));
        root.put("state", authorizationRequest.getState());
        root.set("additionalParameters",objectMapper.valueToTree(authorizationRequest.getAdditionalParameters()));
        root.put("authorizationRequestUri", authorizationRequest.getAuthorizationRequestUri());
        root.set("attributes", objectMapper.valueToTree(authorizationRequest.getAttributes()));
        return JsonUtils.writeValueAsString(root);
    }

    private String encrypt(OAuth2AuthorizationRequest authorizationRequest) {
        return Base64.getEncoder()
                .encodeToString(encryptor.encrypt(serialize(authorizationRequest).getBytes(StandardCharsets.UTF_8)));
    }

    private OAuth2AuthorizationRequest decrypt(String encrypted) {
        return deserialize(new String(encryptor.decrypt(Base64.getDecoder().decode(encrypted)), StandardCharsets.UTF_8));
    }
}
