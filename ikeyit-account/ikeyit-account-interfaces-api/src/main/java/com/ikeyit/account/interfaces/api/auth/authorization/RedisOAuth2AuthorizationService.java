package com.ikeyit.account.interfaces.api.auth.authorization;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String AUTHORIZATIONS_KEY_PREFIX = "oauth2:authorizations:";
    private static final String AUTHORIZATIONS_BY_ID = AUTHORIZATIONS_KEY_PREFIX + "by_id:";
    private static final String AUTHORIZATIONS_BY_STATE = AUTHORIZATIONS_KEY_PREFIX + "by_state:";
    private static final String AUTHORIZATIONS_BY_AUTH_CODE = AUTHORIZATIONS_KEY_PREFIX + "by_code:";
    private static final String AUTHORIZATIONS_BY_ACCESS_TOKEN = AUTHORIZATIONS_KEY_PREFIX + "by_access_token:";
    private static final String AUTHORIZATIONS_BY_REFRESH_TOKEN = AUTHORIZATIONS_KEY_PREFIX + "by_refresh_token:";
    private static final String AUTHORIZATIONS_BY_USER_CODE = AUTHORIZATIONS_KEY_PREFIX + "by_user_code:";
    private static final String AUTHORIZATIONS_BY_DEVICE_CODE = AUTHORIZATIONS_KEY_PREFIX + "by_device_code:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final RegisteredClientRepository registeredClientRepository;

    public RedisOAuth2AuthorizationService(RedisTemplate<String, Object> redisTemplate, 
                                          RegisteredClientRepository registeredClientRepository) {
        Assert.notNull(redisTemplate, "redisTemplate cannot be null");
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.redisTemplate = redisTemplate;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        
        // Store the authorization
        String id = authorization.getId();
        redisTemplate.opsForValue().set(AUTHORIZATIONS_BY_ID + id, authorization);
        
        // Set TTL based on token expiry
        setExpirationIfNeeded(AUTHORIZATIONS_BY_ID + id, authorization);
        
        // Create additional indices for lookup by token values
        if (authorization.getAttribute(OAuth2ParameterNames.STATE) != null) {
            String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
            redisTemplate.opsForValue().set(AUTHORIZATIONS_BY_STATE + state, id);
            setExpirationIfNeeded(AUTHORIZATIONS_BY_STATE + state, authorization);
        }
        
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = 
                authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null && authorizationCode.getToken().getTokenValue() != null) {
            String tokenValue = authorizationCode.getToken().getTokenValue();
            redisTemplate.opsForValue().set(AUTHORIZATIONS_BY_AUTH_CODE + tokenValue, id);
            setExpirationIfNeeded(AUTHORIZATIONS_BY_AUTH_CODE + tokenValue, authorization);
        }
        
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = 
                authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null && accessToken.getToken().getTokenValue() != null) {
            String tokenValue = accessToken.getToken().getTokenValue();
            redisTemplate.opsForValue().set(AUTHORIZATIONS_BY_ACCESS_TOKEN + tokenValue, id);
            setExpirationIfNeeded(AUTHORIZATIONS_BY_ACCESS_TOKEN + tokenValue, authorization);
        }
        
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = 
                authorization.getToken(OAuth2RefreshToken.class);
        if (refreshToken != null && refreshToken.getToken().getTokenValue() != null) {
            String tokenValue = refreshToken.getToken().getTokenValue();
            redisTemplate.opsForValue().set(AUTHORIZATIONS_BY_REFRESH_TOKEN + tokenValue, id);
            setExpirationIfNeeded(AUTHORIZATIONS_BY_REFRESH_TOKEN + tokenValue, authorization);
        }
        
        OAuth2Authorization.Token<OidcIdToken> oidcToken = 
                authorization.getToken(OidcIdToken.class);
        if (oidcToken != null && oidcToken.getToken().getTokenValue() != null) {
            String tokenValue = oidcToken.getToken().getTokenValue();
            redisTemplate.opsForValue().set(AUTHORIZATIONS_BY_ACCESS_TOKEN + tokenValue, id);
            setExpirationIfNeeded(AUTHORIZATIONS_BY_ACCESS_TOKEN + tokenValue, authorization);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        
        // Remove all indices
        String id = authorization.getId();
        redisTemplate.delete(AUTHORIZATIONS_BY_ID + id);
        
        if (authorization.getAttribute(OAuth2ParameterNames.STATE) != null) {
            String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
            redisTemplate.delete(AUTHORIZATIONS_BY_STATE + state);
        }
        
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = 
                authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null && authorizationCode.getToken().getTokenValue() != null) {
            String tokenValue = authorizationCode.getToken().getTokenValue();
            redisTemplate.delete(AUTHORIZATIONS_BY_AUTH_CODE + tokenValue);
        }
        
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = 
                authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null && accessToken.getToken().getTokenValue() != null) {
            String tokenValue = accessToken.getToken().getTokenValue();
            redisTemplate.delete(AUTHORIZATIONS_BY_ACCESS_TOKEN + tokenValue);
        }
        
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = 
                authorization.getToken(OAuth2RefreshToken.class);
        if (refreshToken != null && refreshToken.getToken().getTokenValue() != null) {
            String tokenValue = refreshToken.getToken().getTokenValue();
            redisTemplate.delete(AUTHORIZATIONS_BY_REFRESH_TOKEN + tokenValue);
        }
        
        OAuth2Authorization.Token<OidcIdToken> oidcToken = 
                authorization.getToken(OidcIdToken.class);
        if (oidcToken != null && oidcToken.getToken().getTokenValue() != null) {
            String tokenValue = oidcToken.getToken().getTokenValue();
            redisTemplate.delete(AUTHORIZATIONS_BY_ACCESS_TOKEN + tokenValue);
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return (OAuth2Authorization) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_ID + id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        
        String authorizationId = null;
        
        if (tokenType == null) {
            // Try all token types
            authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_ACCESS_TOKEN + token);
            if (authorizationId == null) {
                authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_REFRESH_TOKEN + token);
            }
            if (authorizationId == null) {
                authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_AUTH_CODE + token);
            }
            if (authorizationId == null) {
                authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_STATE + token);
            }
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_ACCESS_TOKEN + token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_REFRESH_TOKEN + token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_STATE + token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            authorizationId = (String) redisTemplate.opsForValue().get(AUTHORIZATIONS_BY_AUTH_CODE + token);
        }
        
        if (authorizationId != null) {
            return findById(authorizationId);
        }
        
        return null;
    }
    
    private void setExpirationIfNeeded(String key, OAuth2Authorization authorization) {
        long maxTokenValidity = getMaxTokenExpiry(authorization);
        if (maxTokenValidity > 0) {
            redisTemplate.expire(key, maxTokenValidity, TimeUnit.SECONDS);
        }
    }
    
    private long getMaxTokenExpiry(OAuth2Authorization authorization) {
        long maxExpiresIn = 0;
        List<OAuth2Authorization.Token<?>> tokens = new ArrayList<>();
        
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = 
                authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            tokens.add(authorizationCode);
        }
        
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = 
                authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null) {
            tokens.add(accessToken);
        }
        
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = 
                authorization.getToken(OAuth2RefreshToken.class);
        if (refreshToken != null) {
            tokens.add(refreshToken);
        }
        
        OAuth2Authorization.Token<OidcIdToken> oidcToken = 
                authorization.getToken(OidcIdToken.class);
        if (oidcToken != null) {
            tokens.add(oidcToken);
        }
        
        for (OAuth2Authorization.Token<?> token : tokens) {
            if (token.getToken().getExpiresAt() != null) {
                long expiresIn = Duration.between(
                        Objects.requireNonNull(token.getToken().getIssuedAt()),
                        Objects.requireNonNull(token.getToken().getExpiresAt())).getSeconds();
                if (expiresIn > maxExpiresIn) {
                    maxExpiresIn = expiresIn;
                }
            }
        }
        
        return maxExpiresIn;
    }
} 