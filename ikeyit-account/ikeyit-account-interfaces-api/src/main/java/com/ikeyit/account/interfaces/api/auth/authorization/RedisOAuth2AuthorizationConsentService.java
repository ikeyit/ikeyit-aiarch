package com.ikeyit.account.interfaces.api.auth.authorization;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static final String CONSENTS_KEY_PREFIX = "oauth2:consents:";
    private static final String CONSENTS_BY_ID = CONSENTS_KEY_PREFIX + "by_id:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final RegisteredClientRepository registeredClientRepository;

    public RedisOAuth2AuthorizationConsentService(RedisTemplate<String, Object> redisTemplate,
                                                 RegisteredClientRepository registeredClientRepository) {
        Assert.notNull(redisTemplate, "redisTemplate cannot be null");
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.redisTemplate = redisTemplate;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        
        String key = getKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        redisTemplate.opsForValue().set(key, authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        
        String key = getKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        redisTemplate.delete(key);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        
        String key = getKey(registeredClientId, principalName);
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue().get(key);
    }

    private String getKey(String registeredClientId, String principalName) {
        return CONSENTS_BY_ID + registeredClientId + ":" + principalName;
    }
} 